package com.example.jwtpractice.dto;

import com.example.jwtpractice.entity.Member;
import com.example.jwtpractice.entity.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberDto implements UserDetails, OAuth2User {

    private String username;

    private String password;

    private String nickname;

    private String mail;

    private String school;

    private String major;

    private Set<Role> roleSet = new HashSet<>();

    private boolean fromSocial;

    private Map<String, Object> attr;

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;

    public static MemberDto of(Member member){
        return MemberDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .createAt(member.getCreateAt())
                .modifiedAt(member.getModifiedAt())
                .roleSet(member.getRoleSet())
                .fromSocial(member.isFromSocial())
                .mail(member.getMail())
                .build();
    }
    @Override
    public String getName() {
        return nickname;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attr;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleSet.stream().map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
