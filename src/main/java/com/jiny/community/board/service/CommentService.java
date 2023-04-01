package com.jiny.community.board.service;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.domain.UserAccount;
import com.jiny.community.board.domain.Comment;
import com.jiny.community.board.dto.CommentDto;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.board.event.CommentCreatedEvent;
import com.jiny.community.board.repository.CommentRepository;
import com.jiny.community.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long addComment(Long userId,Long postId,String content){
        log.debug("CommentService - 댓글 추가");
        Comment comment = new Comment();
        comment.setAccount(accountRepository.findById(userId).get());
        comment.setPost(postRepository.findById(postId).get());
        comment.setContent(content);

        eventPublisher.publishEvent(new CommentCreatedEvent(comment)); //커멘트 정보와 함께 이벤트 발생
        commentRepository.save(comment);

        Long id = comment.getId();
        return id;
    }

    public List<CommentDto> findCommentList(Long postId){
        List<Comment> list=commentRepository.findByPostId(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for(int i =0;i<list.size();i++){
            CommentDto commentDto = new CommentDto();
            Comment comment = list.get(i);
            commentDto.setId(comment.getId());

            Account commentAccount = comment.getAccount();
            commentDto.setNickname(commentAccount.getNickname());
            commentDto.setContent(comment.getContent());
            commentDto.setCreatedDate(comment.getCreatedDate());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account CurrentAccount;
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserDetails){
                UserAccount userAccount  = (UserAccount)principal;
                CurrentAccount = userAccount.getAccount();
            }else{
                CurrentAccount = null;
            }


            if(commentAccount.equals(CurrentAccount)){
                commentDto.setDeleteBtn(true);
            }else{
                commentDto.setDeleteBtn(false);
            }

            commentDtos.add(commentDto);
        }
        return  commentDtos;
    }
}
