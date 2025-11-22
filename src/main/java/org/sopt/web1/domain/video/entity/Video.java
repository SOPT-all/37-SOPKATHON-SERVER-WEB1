package org.sopt.web1.domain.video.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.global.common.entity.SoftDeleteEntity;

@Entity
@Table(name = "videos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video extends SoftDeleteEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long videoId;

    @Column(name = "video_url", length = 1000)
    private String videoUrl;

    @Column(name = "score")
    private Integer score;

    @Column(name = "content", length = 50)
    private String content;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Video(String videoUrl, Integer score, String content, Member member) {
        this.videoUrl = videoUrl;
        this.score = score;
        this.content = content;
        this.member = member;
    }
}
