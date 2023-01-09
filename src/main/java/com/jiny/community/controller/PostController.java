package com.jiny.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    @GetMapping(value = "/add")
    public String getAddPostPage(){

        return "addPost";
    }

}
