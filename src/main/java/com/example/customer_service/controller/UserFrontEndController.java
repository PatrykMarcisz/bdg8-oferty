package com.example.customer_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserFrontEndController {

    @GetMapping("/")
    public String index(){
        return "index";     // nazwa widoku .html do wyświetlenia
    }
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

}
