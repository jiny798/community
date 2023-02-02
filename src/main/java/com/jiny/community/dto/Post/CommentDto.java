package com.jiny.community.dto.Post;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private String nickname;
}
