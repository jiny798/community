package com.jiny.community.dto;

import lombok.Data;

@Data
public class PostDto {

    public Long id;
    public String nickname;
    public String title;
    public String content;
    public String category;
    public Long starCnt;

}
