package com.jiny.community.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PostDetailResponseDto {
    public Long id;
    public String nickname;
    public String title;
    public String content;
    public String category;
    public Long starCnt;
}
