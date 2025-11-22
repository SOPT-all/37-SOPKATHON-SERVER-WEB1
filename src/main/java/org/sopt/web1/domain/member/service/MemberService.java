package org.sopt.web1.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.member.repository.MemberRepository;
import org.sopt.web1.global.exception.ErrorCode;
import org.sopt.web1.global.exception.handler.MemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
