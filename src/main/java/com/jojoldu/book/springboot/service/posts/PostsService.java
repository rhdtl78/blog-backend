package com.jojoldu.book.springboot.service.posts;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsListResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        
        return new PostsResponseDto(entity);
    }


    @Transactional
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc() // 내림차순 조회
            .stream() // sequence 추출
            .map(PostsListResponseDto::new) // 각 원소를 Dto로 wrapping
            .collect(Collectors.toList()); // 결과를 List로 반환
    }
}