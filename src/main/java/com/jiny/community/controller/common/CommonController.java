package com.jiny.community.controller.common;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.support.CurrentUser;
import com.jiny.community.board.dto.CategoryResponseDto;
import com.jiny.community.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ControllerAdvice @Slf4j
@RequiredArgsConstructor
public class CommonController {
    private final CategoryService categoryService;
    private final AccountRepository accountRepository;

    @ModelAttribute("category_list")
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomError> handleNotFoundException(NotFoundException notFoundException, ServletWebRequest webRequest){
        return ResponseEntity.status(notFoundException.getStatusCode())
                .body(CustomError.builder()
                        .time(LocalDateTime.now().toString())
                        .status(notFoundException.getRawStatusCode())
                        .error(notFoundException.getStatusCode().toString())
                        .message(notFoundException.getMessage())
                        .path(webRequest.getRequest().getRequestURI())
                        .build());
    }
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(@CurrentUser Account account, HttpServletRequest request,RuntimeException ex){
        log.info(getNickname(account)+ "request uri = {}",request.getRequestURI());
        log.error("bad request",ex);
        return "error";
    }

    private String getNickname(Account account){
        return Optional.ofNullable(account)
                .map(Account::getNickname)
                .orElse("");
    }
}
