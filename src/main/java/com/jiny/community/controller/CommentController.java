package com.jiny.community.controller;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.UserAccount;
import com.jiny.community.dto.CommentDto;
import com.jiny.community.dto.PostForm;
import com.jiny.community.repository.AccountRepository;
import com.jiny.community.repository.CommentRepository;
import com.jiny.community.repository.PostRepository;
import com.jiny.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller @Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final CommentService commentService;


    @PostMapping(value = "/post/{id}/comment")
    public String createPost(CommentDto commentDto, Model model, @PathVariable("id")Long postId, Authentication authentication){

        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        Account account = accountRepository.findByNickname(userAccount.getAccountNickName())  ;

        commentService.addComment(account.getId(),postId,commentDto.getContent());

        log.info("content ============="+ commentDto.getContent());
        log.info("사이즈 "+commentService.findCommentList(postId).size());
        model.addAttribute("commentList",commentService.findCommentList(postId));


        return "detailPage :: #commentList";

    }



}
