package com.jiny.community.service;

import com.jiny.community.domain.Post;
import com.jiny.community.dto.PostDto;
import com.jiny.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //게시글 등록
//    @Transactional
//    public Long addPost(String email, PostDto postDto){ //
//       // Post post =
//
//    }



    //게시글 전체 가져와서 dto로 변경
//    public List<Post> postList(){
//      return postRepository.findAll();
//    }

    //


    //게시글 검색 로직



}
