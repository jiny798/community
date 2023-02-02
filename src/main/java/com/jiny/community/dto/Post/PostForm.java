package com.jiny.community.dto.Post;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PostForm {

    @NotBlank(message ="제목을 입력해주세요")
    String title;
    String category;
    @NotBlank(message ="내용을 입력해주세요")
    String content;
}
