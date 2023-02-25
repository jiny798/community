package com.jiny.community.board.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.domain.UserAccount;
import com.jiny.community.board.dto.CommentDto;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.board.repository.CommentRepository;
import com.jiny.community.board.repository.PostRepository;
import com.jiny.community.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public String createComment(@Validated CommentDto commentDto, Model model, BindingResult result, @PathVariable("id")Long postId, Authentication authentication){

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "detailPage :: #commentList";
        }
        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        Account account = accountRepository.findByNickname(userAccount.getAccountNickName())  ;

        commentService.addComment(account.getId(),postId,commentDto.getContent());

        log.debug("content ============="+ commentDto.getContent());
        log.debug("사이즈 "+commentService.findCommentList(postId).size());
        model.addAttribute("commentList",commentService.findCommentList(postId));


        return "detailPage :: #commentList";

    }



}
