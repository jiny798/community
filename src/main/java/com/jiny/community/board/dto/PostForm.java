package com.jiny.community.board.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PostForm {

    @NotBlank(message ="제목을 입력해주세요")
    String title;
    
    @NotBlank(message = "카테고리를 입력해주세요")
    String category;
    @NotBlank(message ="내용을 입력해주세요")
    String content;

    String imgurl;
}
