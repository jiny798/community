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
import com.jiny.community.board.service.CommentService;
import com.jiny.community.board.service.PageService;
import com.jiny.community.board.service.PostService;
import com.jiny.community.account.service.UserService;
import com.jiny.community.api.exception.ApiCustomException;
import com.jiny.community.exception.NotFoundException;
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
    private final CommentService commentService;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final PageService pageService;
    private final ResponseService responseService;

    //게시글 목록
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

    //게시글 상세
    @GetMapping(value = "/{id}")
    public String getPostDetail(@PathVariable("id") Long postId, Model model,Authentication authentication,@CurrentUser Account account){
        checkLikePost(postId, model, account);
        PostDetailResponseDto post = postService.getDetail(postId);
        checkOwnerPost(postId,model,account);
        model.addAttribute("post",post); //postDto 전달

        //댓글 불러오기
        List<CommentDto> commentDtoList = commentService.findCommentList(postId);
        model.addAttribute("commentList",commentDtoList);


        return "detailPage";
    }

    private void checkOwnerPost(Long postId, Model model, Account account) {
        Post findpost = postRepository.findById(postId).orElseThrow(()->new RuntimeException());
        if(findpost.getAccount().equals(account)){
            model.addAttribute("owner",true);
        }else{
            model.addAttribute("owner",false);
        }

    }

    private void checkLikePost(Long postId, Model model, Account account) {
        if(account != null) {
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
            if(userService.isLikePost(account.getId(), postId)){ //좋아요 했던 게시물인가
                model.addAttribute("star",1);
            }else {
                model.addAttribute("star",-1);
            }
        }else{
            model.addAttribute("star",-1); // 좋아요 미체크
        }
    }

    //게시글 등록 폼 가져오기
    @GetMapping(value = "/add")
    public String postForm(Model model){
        model.addAttribute("postform",new PostForm());
        ArrayList<CategoryResponseDto> list = (ArrayList<CategoryResponseDto>)categoryService.getCategoryNames();
        model.addAttribute("categoryList",list);
        return "addPost";
    }

    //게시글 등록 요청
    @PostMapping(value = "/add")
    public String createPost(Model model,@Validated @ModelAttribute("postform") PostForm form, BindingResult result,@CurrentUser Account account) throws UnsupportedEncodingException {

        log.debug("imgurl=={} ",form.getImgurl());
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
    public CommonResult likePost(Model model, @CurrentUser Account account, @PathVariable("id")Long postId){ //스프링 시큐리티 사용시 회원정보 받을 수 있음.
        log.info("user like post = {}",postId);
        if(account==null){
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,"로그인이 필요합니다.");
            //return responseService.getFailResult();
        }else {
            userService.updateLikePost(account.getId(), postId); // userId or user 전달 선택
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

        if(!account.getEmail().equals(post.getAccount().getEmail())){ //post와 account Email이 다르면 해당 로직 실행
            log.info("수정 권한이 없습니다.");
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
        if(!account.getEmail().equals(post.getAccount().getEmail())){ //post와 account Email이 다르면 해당 로직 실행
            log.info("수정 권한이 없습니다.");
            return "board";
        }
        postService.updatePost(postId, postUpdateForm);

        return "redirect:/post/" + postId;
    }

    @PostMapping(value = "/{id}/delete")
    public String deletePost(Model model,@PathVariable("id") Long postId,  @CurrentUser Account account) throws UnsupportedEncodingException {
        log.info("account={} delete postID = {}",account,postId);
        Post post =  postRepository.findById(postId).orElseThrow(() -> new NotFoundException(HttpStatus.BAD_REQUEST,"해당 게시글은 이미 삭제되었습니다."));
        String categoryName = post.getCategory().getName();

        postRepository.delete(post);

        String str = URLEncoder.encode(categoryName, "UTF-8");
        return "redirect:/post/list/"+str;
    }

    @GetMapping("/search")
    public String searchPost(String keyword, Model model,@PageableDefault(size = 5) Pageable pageable){
        log.debug("게시물 검색 {} 페이지 번호 {}",keyword,pageable.getPageNumber());
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
