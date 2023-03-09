package com.jiny.community.common.controller.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ApiCustomException extends HttpStatusCodeException {
    public ApiCustomException(HttpStatus httpStatus,String message){
        super(httpStatus,message);
    }
}
