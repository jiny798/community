package com.jiny.community.exception;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.api.CommonResult;
import com.jiny.community.api.ResponseService;
import com.jiny.community.admin.service.CategoryService;
import com.jiny.community.api.exception.ApiCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@ControllerAdvice @Slf4j
@RequiredArgsConstructor
public class ExControllerAdvice {
    private final CategoryService categoryService;
    private final AccountRepository accountRepository;
    private final ResponseService responseService;

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(@CurrentUser Account account, HttpServletRequest request,RuntimeException ex,Model model){
        log.info(getNickname(account)+ "request uri = {}",request.getRequestURI());
        log.error("bad request",ex);
        model.addAttribute("message",ex.getMessage());
        return "error/error";
    }

    @ExceptionHandler(NotFoundException.class) //ResponseEntity<CustomError>
    public String handleNotFoundException(NotFoundException notFoundException, ServletWebRequest webRequest, Model model){
        model.addAttribute("message",notFoundException.getMessage().toString().substring(3));
        return "error/error";
    }

    private String getNickname(Account account){
        return Optional.ofNullable(account)
                .map(Account::getNickname)
                .orElse("");
    }
}
