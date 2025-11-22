package org.sopt.web1.domain.member.dto;

import java.util.List;

public record MyPageResponse(
        Long memberId,
        String nickname,
        List<MyPageVideoItem> list
) {
    public static MyPageResponse of(Long memberId, String nickname, List<MyPageVideoItem> list) {
        return new MyPageResponse(memberId, nickname, list);
    }

    public record MyPageVideoItem(
            Long memberId,
            String nickname,
            Long videoId,
            String videoUrl,
            String thumbnailUrl,
            Integer likeCount
    ) {
        public static MyPageVideoItem of(Long memberId, String nickname,
                                         Long videoId, String videoUrl, String thumbnailUrl, Integer likeCount) {
            return new MyPageVideoItem(memberId, nickname, videoId, videoUrl, thumbnailUrl, likeCount);
        }
    }
}
