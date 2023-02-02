package com.jiny.community.controller;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.Comment;
import com.jiny.community.domain.Post;
import com.jiny.community.domain.UserAccount;
import com.jiny.community.dto.Post.*;
import com.jiny.community.repository.CategoryRepository;
import com.jiny.community.repository.CommentRepository;
import com.jiny.community.repository.PostRepository;
import com.jiny.community.repository.AccountRepository;
import com.jiny.community.service.CategoryService;
import com.jiny.community.service.Post.PageService;
import com.jiny.community.service.Post.PostService;
import com.jiny.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final PageService pageService;

    //게시글 목록
    @GetMapping(value = "/list/{str}")
    public String postList(Model model,
                           @PathVariable String str,
                           @RequestParam(required = false, defaultValue = "1", value = "page") int pageNo,
                           Pageable pageable){
        pageNo =  pageNo - 1;
        //List<PostResponseDto> posts = postService.getPostList(str);
        Page<PostResponseDto> postList =postService.getPagingList(pageable,pageNo,str,"id");
        PageDto pageDto = pageService.getPageInfo(postList);//Page 에 현재 페이지가 있는데 pageNo를 따로 넣어야하나?

        log.info("page = {}",pageDto);
        model.addAttribute("posts",postList.getContent());
        model.addAttribute("pageDto",pageDto);
        model.addAttribute("categoryName",str);
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

        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (int i=0;i<comments.size();i++){
            CommentDto commentDto = new CommentDto();
            commentDto.setId(comments.get(i).getId());
            commentDto.setNickname(comments.get(i).getAccount().getNickname());
            commentDto.setContent(comments.get(i).getContent());
            commentDtos.add(commentDto);
        }
        model.addAttribute("commentList",commentDtos);


        return "detailPage";
    }


    //게시글 등록 폼 가져오기
    @GetMapping(value = "/add")
    public String postForm(Model model){
        model.addAttribute("postform",new PostForm());
        ArrayList<String> list = (ArrayList<String>)categoryService.getCategoryNames();
        model.addAttribute("categoryList",list);
        return "addPost";
    }

    //게시글 등록 요청
    @PostMapping(value = "/add")
    public String createPost(@Valid PostForm form, Authentication authentication) throws UnsupportedEncodingException {

        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        Account account = accountRepository.findByNickname(userAccount.getAccountNickName())  ;

        postService.addPost(account,form);

        String str = URLEncoder.encode(form.getCategory(), "UTF-8");
        return "redirect:/post/list/"+str;

    }

    @PostMapping(value = "/{id}/like")
    public String likePost(Model model,@PathVariable("id")Long postId,Authentication authentication){ //스프링 시큐리티 사용시 회원정보 받을 수 있음.
        log.info("user like post = {}",postId);

        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
//        if(userAccount==null){
//            log.info("not login");
//            return "detailPage :: #likebtn";
//        }
        Account account = accountRepository.findByNickname(userAccount.getAccountNickName());

        userService.updateLikePost(account.getId(),postId); // userId or user 전달 선택


        return "detailPage :: #likebtn";
    }

    @GetMapping(value = "/{id}/edit")
    public String postEditForm(@PathVariable("id") Long postId, Model model,Authentication authentication){
        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        Account account = accountRepository.findByEmail(userAccount.getAccountEmail());
        Post post = (Post) postRepository.findById(postId).get();

        if(!account.getEmail().equals(post.getAccount().getEmail())){ //post와 account Email이 다르면 해당 로직 실행
            log.info("수정 권한이 없습니다.");
            return "board";
        }

        PostDetailResponseDto form = new PostDetailResponseDto();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());

        model.addAttribute("form", form);

        return "postEditPage";
    }

    @PostMapping(value = "/{id}/edit")
    public String updatePost(@PathVariable("id") Long postId, @ModelAttribute("form") PostResponseDto postResponseDto, Authentication authentication) {
        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        Account account = accountRepository.findByEmail(userAccount.getAccountEmail());
        Post post = (Post) postRepository.findById(postId).get();
        if(!account.getEmail().equals(post.getAccount().getEmail())){ //post와 account Email이 다르면 해당 로직 실행
            log.info("수정 권한이 없습니다.");
            return "board";
        }
        postService.updatePost(postId, postResponseDto);

        return "redirect:/post/" + postId;
    }

}
