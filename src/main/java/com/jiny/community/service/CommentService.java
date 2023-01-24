package com.jiny.community.service;

import com.jiny.community.domain.Comment;
import com.jiny.community.dto.CommentDto;
import com.jiny.community.repository.AccountRepository;
import com.jiny.community.repository.CommentRepository;
import com.jiny.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addComment(Long userId,Long postId,String content){

        Comment comment = new Comment();
        comment.setAccount(accountRepository.findOne(userId));
        comment.setPost(postRepository.findOne(postId));
        comment.setContent(content);

        commentRepository.save(comment);
    }

    public List<CommentDto> findCommentList(Long postId){
        List<Comment> list=commentRepository.findByPostId(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for(int i =0;i<list.size();i++){
            CommentDto commentDto = new CommentDto();
            Comment comment = list.get(i);
            commentDto.setId(comment.getId());
            commentDto.setNickname(comment.getAccount().getNickname());
            commentDto.setContent(comment.getContent());
            commentDtos.add(commentDto);
        }
        return  commentDtos;
    }
}
