package com.jiny.community.board.service;

import com.jiny.community.board.dto.PageDto;
import com.jiny.community.board.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageService {

    public PageDto getPageInfo(Page<PostResponseDto> postList ) {
        int pageNo = postList.getNumber();
        int totalPage = postList.getTotalPages();
        // 현재 페이지를 통해 현재 페이지 그룹의 시작 페이지를 구함
        int startNumber = (int)((Math.floor(pageNo/5)*5)+1 <= totalPage ? (Math.floor(pageNo/5)*5)+1 : totalPage);

        // 전체 페이지 수와 현재 페이지 그룹의 시작 페이지를 통해 현재 페이지 그룹의 마지막 페이지를 구함
        int endNumber = (startNumber + 4 < totalPage ? startNumber + 4 : totalPage);
        boolean hasPrev = postList.hasPrevious();
        boolean hasNext = postList.hasNext();

        /* 화면에는 원래 페이지 인덱스+1 로 출력됨을 주의 */
        int prevIndex = postList.previousOrFirstPageable().getPageNumber()+1;
        int nextIndex = postList.nextOrLastPageable().getPageNumber()+1;

        return new PageDto(totalPage, startNumber, endNumber, hasPrev, hasNext, prevIndex, nextIndex);
    }
}
