package com.jiny.community.api.exception;

import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.admin.service.CategoryService;
import com.jiny.community.api.CommonResult;
import com.jiny.community.api.ResponseService;
import com.jiny.community.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiControllerAdvice {

    private final CategoryService categoryService;
    private final AccountRepository accountRepository;
    private final ResponseService responseService;

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<CustomError> handleNotFoundException(NotFoundException notFoundException, ServletWebRequest webRequest, Model model){
//        return ResponseEntity.status(notFoundException.getStatusCode())
//                .body(CustomError.builder()
//                        .time(LocalDateTime.now().toString())
//                        .status(notFoundException.getRawStatusCode())
//                        .error(notFoundException.getStatusCode().toString())
//                        .message(notFoundException.getMessage())
//                        .path(webRequest.getRequest().getRequestURI())
//                        .build());
//    }

    @ExceptionHandler(ApiCustomException.class)
    @ResponseBody
    public CommonResult handleApiCustomErrorException(ApiCustomException apiCustomException, ServletWebRequest webRequest, Model model){
        return responseService.getFailResult(apiCustomException.getMessage().toString());
    }
}
