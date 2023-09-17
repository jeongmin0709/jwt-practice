package com.example.jwtpractice.security;

import com.example.jwtpractice.dto.MemberDto;
import com.example.jwtpractice.entity.Role;
import com.example.jwtpractice.exception.ErrorCode;
import com.example.jwtpractice.exception.custom_exception.ExpiredJwtTokenException;
import com.example.jwtpractice.exception.custom_exception.InvalidJwtTokenException;
import com.example.jwtpractice.exception.custom_exception.UnsupportedJwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}") private String secretKey;


    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    public String resolveAccessToken(HttpServletRequest request){
        String jwtHeader= request.getHeader("Authorization");
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) return null;
        return jwtHeader.replace("Bearer ", "");
    }



    public String generateAccessToken(MemberDto memberDto){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .setSubject(memberDto.getUsername())
                .claim("roles", memberDto.getRoleSet())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = getClaims(accessToken);

        if (claims.get("roles") == null) throw new InvalidJwtTokenException(ErrorCode.INVALID_TOKEN);

        // 클레임에서 권한 정보 가져오기
        Set<Role> roleSet = Arrays.stream(claims.get("roles").toString().replaceAll("\\[|\\]", "").split(","))
                .map(Role::valueOf).collect(Collectors.toSet());
        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = MemberDto.builder().username(claims.getSubject()).roleSet(roleSet).build();
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    private Claims getClaims(String jwtToken){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
        }
        catch (SecurityException | MalformedJwtException | IllegalArgumentException e){
            log.warn("유효하지 않은 JWT 토큰입니다.", e);
            throw new InvalidJwtTokenException(ErrorCode.INVALID_TOKEN);
        }catch (ExpiredJwtException e){
            log.warn("만료된 JWT 토큰입니다.", e);
            throw new ExpiredJwtTokenException(ErrorCode.EXPIRED_TOKEN);
        }catch (UnsupportedJwtException e){
            log.warn("지원하지 않는 JWT 토큰입니다.", e);
            throw new UnsupportedJwtTokenException(ErrorCode.UNSUPPORTED_TOKEN);
        }
    }
}
