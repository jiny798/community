package com.jiny.community.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponseDto {

    public Long id;
    public String nickname;
    public String title;
    public String content;
    public String category;
    public Long starCnt;

}
