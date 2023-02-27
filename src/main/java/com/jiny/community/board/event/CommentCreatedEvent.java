package com.jiny.community.board.event;

import com.jiny.community.board.domain.Comment;
import lombok.Getter;

@Getter
public class CommentCreatedEvent {
    private final Comment comment;

    public CommentCreatedEvent(Comment comment){
        this.comment = comment;
    }
}
