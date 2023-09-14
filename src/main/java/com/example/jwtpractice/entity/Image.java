package com.example.jwtpractice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = AuditingEntityListener.class)
public class Image {

    @Id
    @Column(name= "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private Member member;

    private String name;

    private String path;

    @CreatedDate
    private LocalDateTime createAt;

    public void setMember(Member member){this.member = member;}

    public String getImageURL(){
        try {
            return URLEncoder.encode(path + File.separator + id + "_" + name, "UTF-8");
        }catch (UnsupportedEncodingException e){
            return "";
        }
    }

}
