package com.jiny.community.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostResponseDto {

    public Long id;
    public String nickname;
    public String title;
    public String content;
    public String category;
    public Long starCnt;
    public String imgUrl;
    public LocalDateTime createdDate;

}
