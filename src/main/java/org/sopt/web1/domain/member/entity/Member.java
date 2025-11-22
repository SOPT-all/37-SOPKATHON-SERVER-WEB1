package org.sopt.web1.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.web1.global.common.entity.SoftDeleteEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "members")
public class Member extends SoftDeleteEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "nickname", unique = true, length = 255)
    private String nickname;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Builder
    public Member(String nickname, String password, String imageUrl) {
        this.nickname = nickname;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public static Member create(String nickname, String encodedPassword, String imageUrl) {
        return Member.builder()
                .nickname(nickname)
                .password(encodedPassword)
                .imageUrl(imageUrl)
                .build();
    }
}
