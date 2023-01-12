package com.jiny.community.controller;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.Post;
import com.jiny.community.domain.UserLikePost;
import com.jiny.community.dto.PostDto;
import com.jiny.community.dto.postForm;
import com.jiny.community.repository.PostRepository;
import com.jiny.community.repository.AccountRepository;
import com.jiny.community.service.PostService;
import com.jiny.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
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
    public String updateItemForm(@PathVariable("id") Long postId, Model model){

        Post post=postRepository.findOne(postId);
        PostDto postDto = new PostDto();
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setNickname(post.getAccount().getNickname());

        model.addAttribute("post",post);

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
    public String createPost(postForm form){
        //임시 User 데이터
        Account account =new Account();
        account.setEmail("abc@abc.com");
        account.setNickname("jiny");
        accountRepository.save(account);

        postService.addPost(account,form.getTitle(),form.getContent());

        return "redirect:/post/list";

    }

    @PostMapping(value = "/{id}/like")
    public void likePost(@PathVariable("id")Long postId  ){ //스프링 시큐리티 사용시 회원정보 받을 수 있음.
        Account account = accountRepository.findByName("abc@abc.com").get(0);
        UserLikePost userLikePost = UserLikePost.createLikePost(postRepository.findOne(postId));

        userService.updateLikePost(account,userLikePost); // userId or user 전달 선택

    }


}
