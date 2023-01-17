package com.jiny.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String getAddPostPage(){
        return "board";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(){
        return "account/login";
    }
}
