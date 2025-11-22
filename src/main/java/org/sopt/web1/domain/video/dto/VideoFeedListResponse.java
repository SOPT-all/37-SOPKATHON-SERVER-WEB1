package org.sopt.web1.domain.video.dto;

import java.util.List;

public record VideoFeedListResponse(
        List<VideoFeedItemResponse> items
) {
    public record VideoFeedItemResponse(
            Long memberId,
            String nickname,
            Long videoId,
            String thumbnailUrl,
            Integer likeCount
    ) {}
}
