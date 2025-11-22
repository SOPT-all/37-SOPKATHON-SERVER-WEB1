package org.sopt.web1.domain.video.dto;

public interface VideoProjection {

    Long getVideoId();
    Long getMemberId();
    String getNickname();
    String getThumbnailUrl();
    Integer getLikeCount();
    Integer getAiScore();
    Double getFinalScore();
}
