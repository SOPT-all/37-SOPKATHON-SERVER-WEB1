package org.sopt.web1.domain.video.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.member.service.MemberService;
import org.sopt.web1.domain.video.dto.AnalysisResultDTO;
import org.sopt.web1.domain.video.dto.VideoAnalysisResponse;
import org.sopt.web1.domain.video.entity.Video;
import org.sopt.web1.global.exception.ErrorCode;
import org.sopt.web1.global.exception.handler.AiException;
import org.sopt.web1.global.exception.handler.ServerException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIAnalysisService {

    private final WebClient openaiWebClient; // OpenAI WebClient로 명확히 변경
    private final ObjectMapper objectMapper; // JSON 파싱을 위해 필요

    // DB 저장을 위한 VideoService/Repository 등을 여기에 주입합니다.
    private final VideoService videoService;
    private final MemberService memberService;

    // OpenAI 모델 사용 (gpt-4o-mini는 멀티모달 분석에 빠르고 적합합니다.)
    private static final String MODEL_NAME = "gpt-4o";
    // OpenAI API 엔드포인트
    private static final String API_ENDPOINT = "/v1/chat/completions";

    /**
     * 동영상 분석 요청을 비동기적으로 수행합니다.
     * @param s3VideoUrl S3에 저장된 동영상 URL
     * @param content 사용자가 입력한 내용
     */
    @Async
    public VideoAnalysisResponse startAnalysis(Long memberId, String s3VideoUrl, String content) {
        System.out.println(">>> [AI 분석 시작] S3 URL: " + s3VideoUrl);

        try {
            // OpenAI 분석 호출 (재시도 포함)
            AnalysisResultDTO result = callOpenAIAnalysisWithRetry(s3VideoUrl);

            double xScore;
            double yScore;
            if (result.getPredictedDrynessScoreX() == null){
                xScore = 0.0;
            } else {
                xScore = result.getPredictedDrynessScoreX();
            }
            if (result.getActualCrunchScoreY() == null){
                yScore = 0.0;
            } else {
                yScore = result.getActualCrunchScoreY();
            }

            int score = (int)Math.round((xScore + yScore)/2);

            Member member = memberService.getMember(memberId);
            Video video = videoService.saveVideo(s3VideoUrl, score, content, member);

            System.out.println(">>> [AI 분석 완료] 결과: " + result);
            return new VideoAnalysisResponse(video.getVideoId(), video.getScore());

        } catch (Exception e) {
            System.err.println(">>> [AI 분석 실패] 오류: " + e.getMessage());
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private AnalysisResultDTO callOpenAIAnalysisWithRetry(String videoUrl) throws Exception {
        Exception lastException = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                return callOpenAIAnalysis(videoUrl); // OpenAI 메서드 호출
            } catch (WebClientResponseException e) {
                // HTTP 오류 처리
                System.err.printf("AI API 호출 실패 (시도 %d): HTTP %d\n", attempt + 1, e.getRawStatusCode());
                lastException = e;
            } catch (Exception e) {
                // 기타 오류 (파싱 오류 등)
                lastException = e;
            }

            // Exponential Backoff (1초, 2초, 4초 대기)
            long delay = (long) Math.pow(2, attempt) * 1000;
            if (attempt < 2) {
                Thread.sleep(delay);
            }
        }
        throw new AiException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private AnalysisResultDTO callOpenAIAnalysis(String videoUrl) throws JsonProcessingException {

        // 1. 프롬프트 및 시스템 정의
        final String systemPrompt =
                "You are an expert leaf crunch analysis AI.\n" +
                        "Your goal is to analyze a leaf-crunching video and score:\n" +
                        "1) Predicted Dryness Score (X) – visual dryness\n" +
                        "2) Actual Crunch Score (Y) – audio crispness\n" +
                        "\n" +
                        "Before scoring:\n" +
                        "- Normalize and enhance audio to reveal subtle crunch peaks. \n" +
                        "- Stabilize visual perception: compensate for camera shake, shadows, and exposure changes.\n" +
                        "- When uncertainty exists, make the most reasonable estimate instead of defaulting to a low score.\n" +
                        "\n" +
                        "Rules:\n" +
                        "- Scores MUST be between 1.0 and 100.\n" +
                        "- If a modality is fully absent (e.g., silent audio or no visible leaves), you MUST assign 1.0 to the missing modality only.\n" +
                        "- Output MUST be a **pure JSON object**, no text, markdown, or explanation.\n" +
                        "\n" +
                        "Your task is to ALWAYS return:\n" +
                        "{\"predictedDrynessScoreX\": NUMBER, \"actualCrunchScoreY\": NUMBER, \"error\": null}\n";

        final String userPrompt = String.format(
                """
                        Analyze the following video URL: {VIDEO_URL}.
                        
                        The subject is someone stepping on fallen leaves.\s
                        Please evaluate the leaf dryness visually and the crispness via audio.
                        
                        Before scoring audio:
                        - Normalize loudness.
                        - Boost low-volume crunch patterns.
                        - Identify transient peaks, high-frequency components, and sharp crackle signatures.
                        - Distinguish crunch from footstep thumps or background noise.
                        
                        Before scoring visuals:
                        - Adjust for lighting, shadows, and camera shake.
                        - Identify leaf color distribution (brown, yellow, green).
                        - Detect dryness indicators: curling, brittleness, fragmentation patterns.
                        
                        SCORING RUBRIC
                        
                        [Predicted Dryness Score (X) – Visual]
                        80–100: Mostly brown/yellow, curled edges, dry texture, brittle fragmentation.
                        40–79: Mixed colors, semi-dry, some moisture, partially curled.
                        1–39: Mostly green, wet/dark surface, smooth texture, unclear view, or blurred video.
                        
                        [Actual Crunch Score (Y) – Audio]
                        80–100: Loud, sharp, high-frequency crackle. Distinct peaks.
                        40–79: Moderate crunch, duller sound, mixed with footstep noise.
                        1–39: Very soft sound, low amplitude, dragging, muffled, or poor audio quality.
                        
                        Return ONLY this JSON:
                        {"predictedDrynessScoreX": NUMBER, "actualCrunchScoreY": NUMBER, "error": null}
                        """, videoUrl
        );

        // --- 2. OpenAI API 요청 본문(Payload) 구성 ---
        final Map<String, Object> payload = Map.of(
                "model", MODEL_NAME,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        // OpenAI는 멀티모달 입력을 messages 배열의 content 배열로 처리합니다.
                        Map.of("role", "user", "content", List.of(
                                Map.of("type", "text", "text", userPrompt),
                                Map.of("type", "text", "text", "Analyze the visual elements from the video and the audio elements (crunch sound). The video URL is valid for multimodal analysis.")
                        ))
                ),
                "response_format", Map.of("type", "json_object"), // JSON 출력 강제
                "max_tokens", 4096
        );

        // --- 3. WebClient 호출 및 응답 파싱 ---
        String jsonResponse = openaiWebClient.post() // WebClient 이름 일치 (openaiWebClient)
                .uri(API_ENDPOINT)
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(String.class)
                .block(Duration.ofSeconds(60)); // 60초 타임아웃 설정

        // WebClient 응답에서 실제 JSON 텍스트 추출 (OpenAI 응답 구조)
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        String resultJsonText = rootNode.at("/choices/0/message/content").asText();

        // JSON 문자열을 DTO로 최종 파싱
        return objectMapper.readValue(resultJsonText, AnalysisResultDTO.class);
    }
}