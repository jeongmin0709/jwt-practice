package com.example.jwtpractice.controller;

import com.example.jwtpractice.entity.Member;
import com.example.jwtpractice.entity.Role;
import com.example.jwtpractice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
public class AuthenticationController {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @PostMapping("/save")
    public String save(){
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
        return "ok";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String user(){
        return "user";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String manager(){
        return "manager";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin(){
        return "admin";
    }

    @GetMapping("/anonymous")
    @PreAuthorize("isAnonymous()")
    public String anonymous(){
        return "anonymous";
    }

}
