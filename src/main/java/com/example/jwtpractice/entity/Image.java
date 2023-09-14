package com.example.jwtpractice.entity;

import jakarta.persistence.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Entity
public class Image {

    @Id
    @Column(name= "IMAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private Member member;

    private String name;

    private String path;

    public void setMember(Member member){this.member = member;}

    public String getImageURL(){
        try {
            return URLEncoder.encode(path + File.separator + id + "_" + name, "UTF-8");
        }catch (UnsupportedEncodingException e){
            return "";
        }
    }

}
