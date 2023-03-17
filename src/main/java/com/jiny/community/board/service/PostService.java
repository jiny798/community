package com.jiny.community.board.service;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.admin.domain.Category;
import com.jiny.community.board.domain.Post;
import com.jiny.community.board.dto.PostDetailResponseDto;
import com.jiny.community.board.dto.PostResponseDto;
import com.jiny.community.board.dto.PostForm;
import com.jiny.community.board.dto.PostUpdateForm;
import com.jiny.community.admin.repository.CategoryRepository;
import com.jiny.community.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    //게시글 등록
    @Transactional
    public Long addPost(Account account, PostForm postForm){ //
        Category category = categoryRepository.findByName(postForm.getCategory());
        Account findAccount = accountRepository.findByNickname(account.getNickname());

        Post post = Post.createPost(findAccount,postForm.getTitle(),postForm.getContent(),category);
        post.setImgUrl(postForm.getImgurl());
        postRepository.save(post);
        return post.getId();

    }

    //게시글 상세 조회
    public PostDetailResponseDto getDetail(Long postId){

        Post post =  postRepository.findById(postId).get();;
        PostDetailResponseDto postResponseDto = new PostDetailResponseDto();
        postResponseDto.setId(postId);
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
        postResponseDto.setNickname(post.getAccount().getNickname());
        postResponseDto.setStarCnt(post.getStar());

        return postResponseDto;
    }

    //게시글 수정
    @Transactional
    public void updatePost(Long postId, PostUpdateForm postUpdateForm){
        Post post =  postRepository.findById(postId).get();
        post.setTitle(postUpdateForm.getTitle());
        post.setCategory(categoryRepository.findByName(postUpdateForm.getCategory()));
        post.setContent(postUpdateForm.getContent());

    }

    public Page<PostResponseDto> getPagingList( int pageNo, String categoryName, String name) {

        Pageable pageable = PageRequest.of(pageNo, 5 , Sort.by(Sort.Direction.DESC, name));
        Category category=categoryRepository.findByName(categoryName);

        Page<Post> page = postRepository.findByCategory(category,pageable);
        Page<PostResponseDto> postList = page.map(p -> new PostResponseDto(p.getId(),
                                            p.getAccount().getNickname(),
                                            p.getTitle(),
                                            p.getContent(),
                                            p.getCategory().getName(),
                                            p.getStar()));
        //page를 유지하면서 Dto변환
        //    public Long id;
        //    public String nickname;
        //    public String title;
        //    public String content;
        //    public String category;
        //    public Long starCnt;
        return postList;
    }

    public Page<PostResponseDto> getPosts(String keyword,Pageable pageable){
        Page<Post> list = postRepository.findByKeyword(keyword,pageable);

        Page<PostResponseDto> posts =  list.map(p -> new PostResponseDto(p.getId(),
                p.getAccount().getNickname(),
                p.getTitle(),
                p.getContent(),
                p.getCategory().getName(),
                p.getStar()));

        return posts;
    }


}
