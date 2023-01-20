package com.jiny.community.controller;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.Post;
import com.jiny.community.domain.UserAccount;
import com.jiny.community.domain.UserLikePost;
import com.jiny.community.dto.PostDto;
import com.jiny.community.dto.postForm;
import com.jiny.community.repository.PostRepository;
import com.jiny.community.repository.AccountRepository;
import com.jiny.community.service.PostService;
import com.jiny.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller @Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final PostService postService;
    private final PostRepository postRepository;

    //게시글 목록
    @GetMapping(value = "/list")
    public String postList(Model model){

        List<Post> post_list = postRepository.findAll();
        ArrayList<PostDto> posts = new ArrayList<>();
        for(int i =0;i<post_list.size();i++){
           PostDto postDto = new PostDto();
           postDto.setTitle(post_list.get(i).getTitle());
           postDto.setContent(post_list.get(i).getContent());
           postDto.setNickname(post_list.get(i).getAccount().getNickname());
           postDto.setId(post_list.get(i).getId());
           posts.add(postDto);
        }
        model.addAttribute("posts",posts);

        return "board";
    }

    //게시글 상세
    @GetMapping(value = "/{id}")
    public String getPostDetail(@PathVariable("id") Long postId, Model model,Authentication authentication){
        model.addAttribute("post",postService.getDetail(postId)); //postDto 전달
        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        if(userAccount == null){
            model.addAttribute("star",0);
        }
        if(userService.isLikePost(userAccount.getAccountId(),postId)){
            model.addAttribute("star",1);
        }else {
            model.addAttribute("star",0);
        }


        return "detailPage";
    }


    //게시글 등록 폼 가져오기
    @GetMapping(value = "/add")
    public String postForm(Model model){
        model.addAttribute("postform",new postForm());
        return "addPost";
    }

    //게시글 등록 요청
    @PostMapping(value = "/add")
    public String createPost(postForm form, Authentication authentication){

        UserAccount userAccount = (UserAccount)authentication.getPrincipal(); // getDetails 는 무엇인지 확인 필요
        Account account = accountRepository.findByNickname(userAccount.getAccountNickName())  ;
        accountRepository.save(account);
        postService.addPost(account,form.getTitle(),form.getContent());

        return "redirect:/post/list";

    }

    @PostMapping(value = "/{id}/like")
    public String likePost(Model model,@PathVariable("id")Long postId,Authentication authentication){ //스프링 시큐리티 사용시 회원정보 받을 수 있음.
        log.info("user like post");

        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        if(userAccount==null){
            log.info("not login");
            return "detailPage :: #likebtn";
        }
        Account account = accountRepository.findByNickname(userAccount.getAccountNickName());

        userService.updateLikePost(account.getId(),postId); // userId or user 전달 선택

        return "detailPage :: #likebtn";
    }


}
