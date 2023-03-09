package com.jiny.community.common.controller.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class NotFoundException extends HttpStatusCodeException {

    public NotFoundException(HttpStatus httpStatus, String message){
        super(httpStatus,message);
    }
}
