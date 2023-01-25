package com.jiny.community.service;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.Category;
import com.jiny.community.domain.Post;
import com.jiny.community.dto.PostDto;
import com.jiny.community.dto.PostForm;
import com.jiny.community.repository.CategoryRepository;
import com.jiny.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    //게시글 등록
    @Transactional
    public Long addPost(Account account, PostForm postForm){ //
        Category category = categoryRepository.findByName(postForm.getCategory());

        Post post = Post.createPost(account,postForm.getTitle(),postForm.getContent(),category);

        return postRepository.save(post);

    }

    //게시글 상세 조회
    public PostDto getDetail(Long postId){
        Post post=postRepository.findOne(postId);
        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setNickname(post.getAccount().getNickname());
        postDto.setStarCnt(post.getStar());
        return postDto;
    }

    //게시글 수정
    @Transactional
    public void updatePost(Long postId,PostDto postDto){
        Post post =postRepository.findOne(postId);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

    }


    public List<PostDto> getPostList(String categoryName){
        Category category = categoryRepository.findByName(categoryName);
        List<Post> post_list = postRepository.findByCategory(category);
        ArrayList<PostDto> posts = new ArrayList<>();
        for(int i =0;i<post_list.size();i++){
            PostDto postDto = new PostDto();
            postDto.setTitle(post_list.get(i).getTitle());
            postDto.setContent(post_list.get(i).getContent());
            postDto.setNickname(post_list.get(i).getAccount().getNickname());
            postDto.setId(post_list.get(i).getId());
            posts.add(postDto);
        }
        return posts;
    }


    //게시글 검색 로직



}
