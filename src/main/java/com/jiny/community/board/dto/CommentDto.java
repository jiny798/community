package com.jiny.community.board.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;

    @NotBlank
    private String content;
    private String nickname;

    private LocalDateTime createdDate;
    private boolean deleteBtn;
}
