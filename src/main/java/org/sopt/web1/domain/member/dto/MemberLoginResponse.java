package org.sopt.web1.domain.member.dto;

import org.sopt.web1.domain.member.entity.Member;

public record MemberLoginResponse(
        Long memberId,
        String nickname
) {
    public static MemberLoginResponse of(Member member) {
        return new MemberLoginResponse(member.getMemberId(), member.getNickname());
    }
}
