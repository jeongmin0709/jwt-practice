package com.example.jwtpractice.security.handler;


import com.example.jwtpractice.dto.ErrorDto;
import com.example.jwtpractice.exception.ErrorCode;
import com.example.jwtpractice.exception.custom_exception.CustomAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException{

        if(exception instanceof CustomAuthenticationException e){
            makeResponse(response, e.getErrorCode());
        }else {
            makeResponse(response, ErrorCode.UNAUTHORIZED);
        }
    }

    private void makeResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = new ErrorDto(errorCode);
        String responseBody = objectMapper.writeValueAsString(errorDto);

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
