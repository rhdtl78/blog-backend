package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {}
/**
 * Entity와 Repository는 아주 밀접한 관계
 * 정상 동작을 위해서는 둘다 필요.
 * 도메인 이전을 한다면 둘은 함께 움직여야함.
 */