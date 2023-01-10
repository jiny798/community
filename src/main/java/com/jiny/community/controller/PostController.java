package com.jiny.community.controller;

import com.jiny.community.domain.Post;
import com.jiny.community.domain.User;
import com.jiny.community.dto.PostDto;
import com.jiny.community.dto.postForm;
import com.jiny.community.repository.PostRepository;
import com.jiny.community.repository.UserRepository;
import com.jiny.community.service.PostService;
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

    private final UserRepository userRepository;
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
           postDto.setNickname(post_list.get(i).getUser().getNickname());
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
        postDto.setNickname(post.getUser().getNickname());

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
        User user =new User();
        user.setEmail("abc@abc.com");
        user.setNickname("jiny");
        userRepository.save(user);

        postService.addPost(user,form.getTitle(),form.getContent());

        return "redirect:/post/list";
    }



}
