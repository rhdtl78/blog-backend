package com.jojoldu.book.springboot.config.auth.dto;

import com.jojoldu.book.springboot.domain.user.User;

import lombok.Getter;

/**
 * SessionUser
 * 
 * 세션에 사용자 정보를 저장하기 위한 Dto
 * 인증된 사용자 정보만 필요하기에 name, email, picture만 저장.
 * 
 * User 클래스를 사용하지 않는 이유?
 *      - '직렬화' 기능때문
 *      - User 클래스는 Entity이기 때문에
 *          - 다른 엔티티와 관계를 맺을 가능성이 있음
 *          - 관계를 맺게 되면 직렬화 대상에 관계 엔티티도 포함되게됨 = 성능 이슈
 */
@Getter
public class SessionUser {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}