package com.example.jwtpractice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = AuditingEntityListener.class)
@ToString
public class Member {

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String mail;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String major;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roleSet = new HashSet<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Image profileImage;


    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public Member(String username, String password, String nickname, String mail, String school, String major) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.mail = mail;
        this.school = school;
        this.major = major;
    }

    public void addRole(Role role){roleSet.add(role);}
}
