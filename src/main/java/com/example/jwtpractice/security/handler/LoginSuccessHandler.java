package com.example.jwtpractice.security.handler;

import com.example.jwtpractice.dto.ErrorDto;
import com.example.jwtpractice.dto.MemberDto;
import com.example.jwtpractice.exception.ErrorCode;
import com.example.jwtpractice.security.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MemberDto memberDto = (MemberDto) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(memberDto);
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("RefreshToken", "Bearer "+ refreshToken);
    }
}
