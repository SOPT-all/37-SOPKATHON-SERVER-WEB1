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

    @Column(name = "nickname", unique = true, length = 20)
    private String nickname;

    @Column(name = "password", length = 20)
    private String password;

    @Builder
    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}
