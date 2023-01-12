package com.jiny.community.service;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.Post;
import com.jiny.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //게시글 등록
    @Transactional
    public Long addPost(Account account, String title, String content){ //

        Post post = Post.createPost(account,title,content);

        return postRepository.save(post);

    }

    //게시글 상세 조회


    //게시글 검색 로직



}