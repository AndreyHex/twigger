package com.twigger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = "/{[path:[^\\.]*}")
    public String redirectApi() {
        return "forward:/";
    }

}