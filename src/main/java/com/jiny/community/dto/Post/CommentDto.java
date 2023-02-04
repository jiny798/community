package com.jiny.community.dto.Post;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDto {
    private Long id;

    @NotBlank
    private String content;
    private String nickname;
}
