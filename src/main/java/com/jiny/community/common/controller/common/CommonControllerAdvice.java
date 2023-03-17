package com.jiny.community.common.controller.common;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.api.CommonResult;
import com.jiny.community.api.ResponseService;
import com.jiny.community.board.dto.CategoryResponseDto;
import com.jiny.community.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@ControllerAdvice @Slf4j
@RequiredArgsConstructor
public class CommonControllerAdvice {
    private final CategoryService categoryService;
    private final AccountRepository accountRepository;
    private final ResponseService responseService;

    @ModelAttribute("category_list")//카테 고리 리스트 반환
    public List<CategoryResponseDto> categoryNames(){
        List<CategoryResponseDto> category_list = categoryService.getCategoryNames();

        return category_list;
    }

    public void commonAccount(Model model, @CurrentUser Account account){
        if(account != null) {
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
        }
    }

    @ExceptionHandler(NotFoundException.class) //ResponseEntity<CustomError>
    public String handleNotFoundException(NotFoundException notFoundException, ServletWebRequest webRequest,Model model){
        model.addAttribute("message",notFoundException.getMessage().toString().substring(3));
        return "error/error";
//        return ResponseEntity.status(notFoundException.getStatusCode())
//                .body(CustomError.builder()
//                        .time(LocalDateTime.now().toString())
//                        .status(notFoundException.getRawStatusCode())
//                        .error(notFoundException.getStatusCode().toString())
//                        .message(notFoundException.getMessage())
//                        .path(webRequest.getRequest().getRequestURI())
//                        .build());
    }

    @ExceptionHandler(ApiCustomException.class)
    @ResponseBody
    public CommonResult handleApiCustomErrorException(ApiCustomException apiCustomException, ServletWebRequest webRequest, Model model){
       return responseService.getFailResult(apiCustomException.getMessage().toString());
    }
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(@CurrentUser Account account, HttpServletRequest request,RuntimeException ex,Model model){
        log.info(getNickname(account)+ "request uri = {}",request.getRequestURI());
        log.error("bad request",ex);
        model.addAttribute("message",ex.getMessage());
        return "error/error";
    }

    private String getNickname(Account account){
        return Optional.ofNullable(account)
                .map(Account::getNickname)
                .orElse("");
    }
}
