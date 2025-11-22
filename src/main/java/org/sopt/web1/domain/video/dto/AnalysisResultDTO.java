package org.sopt.web1.domain.video.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AnalysisResultDTO {

    // 예측 점수 (이미지 분석 기반)
    @JsonProperty("predictedDrynessScoreX")
    private Double predictedDrynessScoreX;

    // 실제 점수 (오디오 분석 기반)
    @JsonProperty("actualCrunchScoreY")
    private Double actualCrunchScoreY;

}