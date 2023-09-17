package com.example.jwtpractice.exception.custom_exception;

import com.example.jwtpractice.exception.ErrorCode;

public class InvalidJwtTokenException extends CustomAuthenticationException {
    public InvalidJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
