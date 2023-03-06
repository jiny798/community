package com.jiny.community.controller.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class NotFoundException extends HttpStatusCodeException {

    protected NotFoundException(HttpStatus httpStatus, String message){
        super(httpStatus,message);
    }
}
