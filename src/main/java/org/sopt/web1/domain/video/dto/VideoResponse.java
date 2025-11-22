package org.sopt.web1.domain.video.dto;

public record VideoResponse(
        Long memberId,
        String nickname,
        String videoUrl,
        String thumbnailUrl,
        int likeCount,
        String content,
        int score
) {
}
