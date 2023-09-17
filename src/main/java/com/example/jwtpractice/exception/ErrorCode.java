package com.example.jwtpractice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-001", "서버 에러입니다."),
    NOT_SUPPORT_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-002", "지원하지 않는 http method 입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-003", "입력값이 유효하지 않습니다"),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "COMMON-004", "입력이 잘못된 형식입니다."),
    NOTFOUND_BOARD(HttpStatus.NOT_FOUND, "BOARD-001", "게시글을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "USER-001", "인증되지 않은 사용자입니다."),
    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "USER-002", null),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "USER-003", "유효하지 않은 JWT 토큰 입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "USER-004", "만료된 JWT 토큰 입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "USER-004", "지원하지 않는 JWT 토큰 입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "USER-002", "접근이 거부되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
