package com.jiny.community.board.dto;

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
