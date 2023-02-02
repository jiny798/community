package com.jiny.community.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDto {
    int totalPage;
    int startNum;
    int endNum;
    boolean hasPrev ;
    boolean hasNext ;
    int prevIndex ;
    int nextIndex ;

}
