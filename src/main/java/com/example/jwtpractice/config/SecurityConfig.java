package com.example.jwtpractice.config;

import com.example.jwtpractice.security.JwtTokenProvider;
import com.example.jwtpractice.security.filter.JwtAuthenticationFilter;
import com.example.jwtpractice.security.filter.JwtAuthorizationFilter;
import com.example.jwtpractice.security.handler.JwtAccessDeniedHandler;
import com.example.jwtpractice.security.handler.JwtAuthenticationEntryPoint;
import com.example.jwtpractice.security.handler.LoginFailureHandler;
import com.example.jwtpractice.security.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CorsConfigurationSource configurationSource;



    public SecurityConfig(@Qualifier("corsConfigurationSource") CorsConfigurationSource configurationSource){
        this.configurationSource = configurationSource;
    }    //jwt 방식
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors-> cors.configurationSource(configurationSource))
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .addFilter(jwtAuthenticationFilter(authenticationManager))
                .addFilter(JwtAuthorizationFilter(authenticationManager))
                .exceptionHandling(handler->handler.authenticationEntryPoint(jwtAuthenticationEntryPoint())
                .accessDeniedHandler(jwtAccessDeniedHandler()));
        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager){
        return new JwtAuthenticationFilter(authenticationManager, loginSuccessHandler(), loginFailureHandler());
    }

    JwtAuthorizationFilter JwtAuthorizationFilter(AuthenticationManager authenticationManager){
        return new JwtAuthorizationFilter(authenticationManager, jwtTokenProvider(), jwtAuthenticationEntryPoint());
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    JwtTokenProvider jwtTokenProvider(){return new JwtTokenProvider();}

    @Bean
    LoginSuccessHandler loginSuccessHandler(){return new LoginSuccessHandler(jwtTokenProvider());}

    @Bean
    LoginFailureHandler loginFailureHandler(){return new LoginFailureHandler();}

    @Bean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(){return new JwtAuthenticationEntryPoint();}

    @Bean
    JwtAccessDeniedHandler jwtAccessDeniedHandler(){return new JwtAccessDeniedHandler();}

}
