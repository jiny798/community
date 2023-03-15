package com.jiny.community.board.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.api.ResponseService;
import com.jiny.community.api.SingleResult;
import com.jiny.community.board.domain.Comment;
import com.jiny.community.board.dto.CommentDto;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.board.repository.CommentRepository;
import com.jiny.community.board.repository.PostRepository;
import com.jiny.community.board.service.CommentService;
import com.jiny.community.common.controller.common.ApiCustomException;
import com.jiny.community.common.controller.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller @Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final CommentService commentService;
    private final ResponseService responseService;

    @PostMapping(value = "/post/{id}/comment")
    @ResponseBody
    public SingleResult createComment(@Validated CommentDto commentDto, @CurrentUser Account account, Model model, BindingResult result, @PathVariable("id")Long postId){
        if(account==null){
             throw new ApiCustomException(HttpStatus.BAD_REQUEST,"로그인이 필요합니다.");
        }
        Long commentId = commentService.addComment(account.getId(),postId,commentDto.getContent());
        return responseService.getSingleResult(commentId);
    }

    @PostMapping(value = "/post/{id}/comment/{commentId}")
    @ResponseBody
    public SingleResult deleteComment(@CurrentUser Account account, Model model,  @PathVariable("id")Long postId,@PathVariable("commentId") String commentId){
        if(account==null){
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,"로그인이 필요합니다.");
        }
        Comment comment = commentRepository.findById(Long.parseLong(commentId)).orElseThrow(()->{
            throw new ApiCustomException(HttpStatus.BAD_REQUEST,"삭제된 댓글입니다.");
        });

        commentRepository.delete(comment);

        return responseService.getSingleResult(commentService.findCommentList(postId));
    }



}
