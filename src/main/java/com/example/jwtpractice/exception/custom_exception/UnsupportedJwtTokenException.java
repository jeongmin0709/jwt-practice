package com.example.jwtpractice.exception.custom_exception;

import com.example.jwtpractice.exception.ErrorCode;

public class UnsupportedJwtTokenException extends CustomAuthenticationException{
    public UnsupportedJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
