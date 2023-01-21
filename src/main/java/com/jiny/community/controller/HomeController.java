package com.jiny.community.controller;

import com.jiny.community.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String home(HttpServletRequest request){
        return "board";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(){
        return "account/login";
    }
}
