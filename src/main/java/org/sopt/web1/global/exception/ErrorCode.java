package org.sopt.web1.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    FILE_INPUT_ERROR(HttpStatus.BAD_REQUEST, 40001, "이미지 파일이 없습니다."),
    FILE_TYPE_ERROR(HttpStatus.BAD_REQUEST, 40002, "동영상 타입만 입력 가능합니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, 40101, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_URL(HttpStatus.NOT_FOUND, 40401, "없는 URL 주소 입니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, 40402, "사용자를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final int code;
    private final String msg;
}
