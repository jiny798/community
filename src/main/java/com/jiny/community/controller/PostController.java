package com.jiny.community.controller;

import com.jiny.community.domain.User;
import com.jiny.community.dto.postForm;
import com.jiny.community.repository.UserRepository;
import com.jiny.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final UserRepository userRepository;
    private final PostService postService;

    //게시글 목록
    @GetMapping(value = "/list")
    public String postList(){

        return "home";
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
        User user =new User();
        user.setEmail("abc@abc.com");
        user.setNickname("jiny");
        userRepository.save(user);

        postService.addPost(user,form.getTitle(),form.getContent());

        return "redirect:/";
    }



}
