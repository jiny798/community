package com.jiny.community.board.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.api.CommonResult;
import com.jiny.community.api.ResponseService;
import com.jiny.community.board.domain.Comment;
import com.jiny.community.board.domain.Post;
import com.jiny.community.account.domain.UserAccount;
import com.jiny.community.board.dto.*;
import com.jiny.community.admin.repository.CategoryRepository;
import com.jiny.community.board.repository.CommentRepository;
import com.jiny.community.board.repository.PostRepository;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.admin.service.CategoryService;
import com.jiny.community.board.service.PageService;
import com.jiny.community.board.service.PostService;
import com.jiny.community.account.service.UserService;
import com.jiny.community.common.controller.common.ApiCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private final ResponseService responseService;

    //????????? ??????
    @GetMapping(value = "/list/{str}")
    public String postList(Model model,
                           @PathVariable String str,
                           @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
                           , HttpServletRequest request, @CurrentUser Account account){
        if(account != null) {
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
        }

        Page<PostResponseDto> postPage =postService.getPagingList(pageNo,str,"id");
        int startNumber=0; int endNumber=0;

        if( postPage.getTotalPages() != 0) {
             startNumber = (int) ((Math.floor(pageNo / 5) * 5) + 1 <= postPage.getTotalPages() ? (Math.floor(pageNo / 5) * 5) : postPage.getTotalPages());
             endNumber = (startNumber + 4 < postPage.getTotalPages() ? startNumber + 4 : postPage.getTotalPages() - 1);
        }
        model.addAttribute("postPage",postPage);
        model.addAttribute("startNumber",startNumber);
        model.addAttribute("endNumber",endNumber);
        model.addAttribute("category",str);



        return "board";
    }

    //????????? ??????
    @GetMapping(value = "/{id}")
    public String getPostDetail(@PathVariable("id") Long postId, Model model,Authentication authentication,@CurrentUser Account account){
        if(account != null) {
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
            if(userService.isLikePost(account.getId(),postId)){ //????????? ?????? ???????????????
                model.addAttribute("star",1);
            }else {
                model.addAttribute("star",-1);
            }
        }else{
            model.addAttribute("star",-1); // ????????? ?????????
        }
        PostDetailResponseDto post = postService.getDetail(postId);
        model.addAttribute("post",post); //postDto ??????

//        UserAccount userAccount = (UserAccount)authentication.getPrincipal();

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

    //????????? ?????? ??? ????????????
    @GetMapping(value = "/add")
    public String postForm(Model model){
        model.addAttribute("postform",new PostForm());
        ArrayList<CategoryResponseDto> list = (ArrayList<CategoryResponseDto>)categoryService.getCategoryNames();
        model.addAttribute("categoryList",list);
        return "addPost";
    }

    //????????? ?????? ??????
    @PostMapping(value = "/add")
    public String createPost(Model model,@Validated @ModelAttribute("postform") PostForm form, BindingResult result,@CurrentUser Account account) throws UnsupportedEncodingException {

        log.debug("account proxy= {}",account.getClass().getName());
        if (result.hasErrors()) {
            log.info("errors={}", result);
            ArrayList<CategoryResponseDto> list = (ArrayList<CategoryResponseDto>)categoryService.getCategoryNames();
            model.addAttribute("categoryList",list);
            return "addPost";
        }

        postService.addPost(account,form);

        String str = URLEncoder.encode(form.getCategory(), "UTF-8");
        return "redirect:/post/list/"+str;

    }
    @PostMapping(value = "/{id}/like")
    @ResponseBody
    public CommonResult likePost(Model model, @CurrentUser Account account, @PathVariable("id")Long postId){ //????????? ???????????? ????????? ???????????? ?????? ??? ??????.
        log.info("user like post = {}",postId);
        if(account==null){
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,"???????????? ???????????????.");
            //return responseService.getFailResult();
        }else {
            userService.updateLikePost(account.getId(), postId); // userId or user ?????? ??????
            return responseService.getSuccessResult();
        }

    }

    @GetMapping(value = "/{id}/edit")
    public String postEditForm(@PathVariable("id") Long postId, Model model,Authentication authentication){
        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        Account account = accountRepository.findByEmail(userAccount.getAccountEmail());
        Post post = (Post) postRepository.findById(postId).get();
        ArrayList<CategoryResponseDto> list = (ArrayList<CategoryResponseDto>)categoryService.getCategoryNames();
        model.addAttribute("categoryList",list);

        if(!account.getEmail().equals(post.getAccount().getEmail())){ //post??? account Email??? ????????? ?????? ?????? ??????
            log.info("?????? ????????? ????????????.");
            return "board";
        }

        PostDetailResponseDto form = new PostDetailResponseDto();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setCategory(post.getCategory().getName());
        form.setContent(post.getContent());

        model.addAttribute("form", form);

        return "postEditPage";
    }

    @PostMapping(value = "/{id}/edit")
    public String updatePost(Model model,@PathVariable("id") Long postId,@Validated @ModelAttribute("form") PostUpdateForm postUpdateForm,BindingResult result, Authentication authentication) {

        if (result.hasErrors()) {
            log.info("errors={}", result);
            ArrayList<CategoryResponseDto> list = (ArrayList<CategoryResponseDto>)categoryService.getCategoryNames();
            model.addAttribute("categoryList",list);
            return "postEditPage";
        }

        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        Account account = accountRepository.findByEmail(userAccount.getAccountEmail());
        Post post = (Post) postRepository.findById(postId).get();
        if(!account.getEmail().equals(post.getAccount().getEmail())){ //post??? account Email??? ????????? ?????? ?????? ??????
            log.info("?????? ????????? ????????????.");
            return "board";
        }
        postService.updatePost(postId, postUpdateForm);

        return "redirect:/post/" + postId;
    }

    @GetMapping("/search")
    public String searchPost(String keyword, Model model,@PageableDefault(size = 5) Pageable pageable){
        log.debug("????????? ?????? {} ????????? ?????? {}",keyword,pageable.getPageNumber());
        Page<PostResponseDto> postPage = postService.getPosts(keyword,pageable);

        int pageNo =pageable.getPageNumber();

        int startNumber=0; int endNumber=0;

        if( postPage.getTotalPages() != 0) {
             startNumber = (int) ((Math.floor(pageNo / 5) * 5) + 1 <= postPage.getTotalPages() ? (Math.floor(pageNo / 5) * 5) : postPage.getTotalPages());
             endNumber = (startNumber + 4 < postPage.getTotalPages() ? startNumber + 4 : postPage.getTotalPages() - 1);
        }
        log.debug("num = {} {} {}",pageNo,startNumber,endNumber);
        model.addAttribute("postPage",postPage);
        model.addAttribute("keyword",keyword);
        model.addAttribute("startNumber",startNumber);
        model.addAttribute("endNumber",endNumber);
        return "board_search";
    }


}
