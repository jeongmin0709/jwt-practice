package com.example.jwtpractice.security.handler;

import com.example.jwtpractice.dto.ErrorDto;
import com.example.jwtpractice.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다.\n 다시 확인해 주세요.";
        } else {
            if (exception instanceof InternalAuthenticationServiceException) {
                errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다.\n 관리자에게 문의하세요.";
            } else if (exception instanceof UsernameNotFoundException) {
                errorMessage = "계정이 존재하지 않습니다.\n 회원가입 진행 후 로그인 해주세요.";
            } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
                errorMessage = "인증 요청이 거부되었습니다.\n 관리자에게 문의하세요.";
            } else {
                errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다.\n 관리자에게 문의하세요.";
            }

        }

        String responseBody = makeResponseBody(errorMessage);
        setResponse(response, responseBody);

    }

    private String makeResponseBody(String errorMessage) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorCode errorCode = ErrorCode.LOGIN_FAILURE;
        ErrorDto errorDto = ErrorDto.builder()
                .code(errorCode.getCode())
                .message(errorMessage)
                .build();
        return objectMapper.writeValueAsString(errorDto);

    }

    private void setResponse(HttpServletResponse response, String responseBody) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);

    }

}
