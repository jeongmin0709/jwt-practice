package com.example.jwtpractice.exception.custom_exception;

import com.example.jwtpractice.exception.ErrorCode;

public class ExpiredJwtTokenException extends CustomAuthenticationException{
    public ExpiredJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
