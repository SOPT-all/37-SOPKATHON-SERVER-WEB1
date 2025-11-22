package org.sopt.web1.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.member.dto.MemberLoginRequest;
import org.sopt.web1.domain.member.dto.MemberLoginResponse;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.member.repository.MemberRepository;
import org.sopt.web1.global.exception.ErrorCode;
import org.sopt.web1.global.exception.handler.MemberException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Transactional
    public MemberLoginResponse login(MemberLoginRequest request) {
        Optional<Member> optionalMember = memberRepository.findByNickname(request.nickname());

        // 1. 해당 닉네임을 가진 회원이 존재할 때
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            // 비밀번호 체크
            if (!passwordEncoder.matches(request.password(), member.getPassword())) {
                throw new MemberException(ErrorCode.INVALID_PASSWORD);
            }

            return MemberLoginResponse.of(member);
        }

        // 2. 해당 닉네임을 가진 회원이 존재하지 않을 때
        String encodedPassword = passwordEncoder.encode(request.password());
        Member newMember = Member.create(request.nickname(), encodedPassword);
        memberRepository.save(newMember);

        return MemberLoginResponse.of(newMember);
    }
}
