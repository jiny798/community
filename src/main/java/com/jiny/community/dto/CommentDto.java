package com.jiny.community.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private String nickname;
}
