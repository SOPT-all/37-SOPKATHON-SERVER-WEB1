package org.sopt.web1.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.member.dto.MemberLoginRequest;
import org.sopt.web1.domain.member.dto.MemberLoginResponse;
import org.sopt.web1.domain.member.service.MemberService;
import org.sopt.web1.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 API", description = "회원 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "회원 로그인/회원가입",
            description = """
            회원 로그인/회원가입 API입니다.
            - RequestBody로 nickname과 password를 전달합니다.
            """
    )
    @PostMapping
    public ResponseEntity<ApiResponse<MemberLoginResponse>> login(
            @RequestBody MemberLoginRequest request
    ) {
        MemberLoginResponse response = memberService.login(request);
        return ResponseEntity.ok(
                ApiResponse.ok(response, "로그인에 성공했습니다.")
        );
    }
}
