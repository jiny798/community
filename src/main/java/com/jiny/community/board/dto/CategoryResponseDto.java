package com.jiny.community.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class CategoryResponseDto implements Serializable {

    private Long id;
    private String name;

}
