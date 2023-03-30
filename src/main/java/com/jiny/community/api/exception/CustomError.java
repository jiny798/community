package com.jiny.community.api.exception;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomError {
    private String time;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
