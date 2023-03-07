package com.jiny.community.api;

import lombok.Data;

@Data
public class CommonResult {
    private boolean success;
    private int code;
    private String msg;
}
