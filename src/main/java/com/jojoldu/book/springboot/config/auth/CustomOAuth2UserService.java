package com.jojoldu.book.springboot.config.auth;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import com.jojoldu.book.springboot.config.auth.dto.OAuthAttributes;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.domain.user.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * CustomOAuth2UserService
 * 
 * 소셜 로그인 성공 이후 사용자 정보를 가져올 때의 설정 담당.
 * 리소스 서버 (즉, 소셜 서비스)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
 * 구글 로그인 이후 가져온 사용자 정보를 기반으로 가입 및 정보 수정, 세션 저장등의 기능 지원
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * 현재 로그인 진행중인 서비스를 구분
         * 이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기위함.
         */
        String registrationId = userRequest
            .getClientRegistration()
            .getRegistrationId();

        /** 
         * OAuth2 로그인 진행시 키가 되는 필드 값 == Primary key
         * 구글의 경우 기본적으로 코드 지원 (기본 sub)
         *      - 그러나 네이버, 카카오의 경우 지원하지 않음.
         * 이후 네이버 로그인과 구글 로그인을 동시 지원시에 사용.
         */
        String userNameAttributeName = userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

        /**
         * OAuth2Service를 통해 가져온 OAuth2User의 attribute를 담을 클래스
         * 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용.
         */
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(user));
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
            attributes.getAttributes(), 
            attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
        .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
        .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
} 