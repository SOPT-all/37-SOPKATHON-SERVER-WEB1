package org.sopt.web1.video.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.video.dto.VideoAnalysisResponse;
import org.sopt.web1.domain.video.service.AIAnalysisService;
import org.sopt.web1.domain.video.service.VideoService;
import org.sopt.web1.global.common.response.ApiResponse;
import org.sopt.web1.global.common.util.S3Uploader;
import org.sopt.web1.global.exception.ErrorCode;
import org.sopt.web1.global.exception.handler.ServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/videos")
public class VideoController {

    private final S3Uploader s3Uploader;
    private final AIAnalysisService aiAnalysisService;
    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<ApiResponse<VideoAnalysisResponse>> uploadAndStartAnalysis(
            @RequestHeader(name = "memberId") Long memberId,
            @RequestParam("video") MultipartFile video,
            @RequestParam("content") String content) {

        String s3Url;
        try {
            // 1. S3 업로드 요청 (업로드 완료 시 URL 반환)
            s3Url = s3Uploader.upload(video);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 2. AI 분석 시작 (비동기 호출)
        // 컨트롤러는 즉시 응답하고, 분석은 백그라운드 스레드에서 진행
        VideoAnalysisResponse response = aiAnalysisService.startAnalysis(memberId, s3Url, content);

        System.out.println("S3 URL: " + s3Url);
        System.out.println(">>> [비동기 분석 시작 요청됨]");

        // 3. 클라이언트에게 성공 메시지 즉시 반환
        return ResponseEntity.ok(ApiResponse.ok(response, "영상 분석이 완료되었습니다."));
    }

    @DeleteMapping("/videos/{videoId}")
    public ResponseEntity<ApiResponse<Void>> deleteVideo(
            @RequestHeader(name = "memberId") Long memberId,
            @PathVariable(name = "videoId") Long videoId
    ){

        videoService.deleteVideo(memberId, videoId);
        return ResponseEntity.ok(ApiResponse.ok(null,"영상이 삭제되었습니다."));
    }
}