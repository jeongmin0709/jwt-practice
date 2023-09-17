package com.example.jwtpractice;

import com.example.jwtpractice.entity.Member;
import com.example.jwtpractice.entity.Role;
import com.example.jwtpractice.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("로그인 필터 테스트")
public class JwtAuthenticationFilterTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setMember(){
        Member member = Member.builder()
                .username("abcde")
                .password(passwordEncoder.encode("1234"))
                .mail("avcd@bbbb")
                .nickname("ddsaa")
                .school("금오공대")
                .major("컴퓨터공학과")
                .build();
        member.addRole(Role.ROLE_USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("로그인 요청")
    public void getBoardPageTest() throws Exception {
        //given
        String url = "/members/login";
        Map<String, String> map = new HashMap<>();
        map.put("username", "abcde");
        map.put("password", "1234");
        String content = objectMapper.writeValueAsString(map);
        //when, then
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
