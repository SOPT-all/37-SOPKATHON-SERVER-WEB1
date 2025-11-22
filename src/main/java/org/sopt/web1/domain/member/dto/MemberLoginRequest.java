package org.sopt.web1.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank(message = "닉네임을 입력해주세요")
        String nickname,
        @NotBlank(message = "비밀번호을 입력해주세요")
        String password
) {
}