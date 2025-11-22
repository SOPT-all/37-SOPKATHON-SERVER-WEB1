package org.sopt.web1.domain.video.dto;

public record VideoAnalysisResponse(
        Long videoId,
        Integer score
) {
}
