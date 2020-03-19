package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

/**
 * SecurityConfig
 * 
 * OAuth를 이용한 소셜 로그인 설정 구현
 * 
 */
@RequiredArgsConstructor
@EnableWebSecurity // SpringSecurity 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // h2-console 사용을 위한 옵션 설정
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
                // url 별 권한 관리를 설정.
                .authorizeRequests()
                // 권한 관리 대상 지정
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // 모두 허용
                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // USER 권한을 가진 사용자만 허용.
                // 이외의 모든 요청에 대해
                .anyRequest().authenticated() // 인증된 사용자들만 허용함. 
            .and()
                // 로그아웃 기능의 설정
                .logout()
                    // 로그아웃 성공시 리다이렉트
                    .logoutSuccessUrl("/")
            .and()
                // oAuth2 로그인 기능 설정
                .oauth2Login()
                    // OAuth2로그인 성공 이후 사용자 정보를 가져올 때의 설정 담당.
                    .userInfoEndpoint()
                        /**
                         * 소셜 로그인 성공 이후 사용자 정보를 가져올 때의 설정 담당.
                         * 리소스 서버 (즉, 소셜 서비스)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
                         */
                        .userService(customOAuth2UserService);
    }
}