package com.example.customer_service.controller;

import com.example.customer_service.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserFrontEndController {

    @GetMapping("/")
    public String index(){
        return "index";     // nazwa widoku .html do wyświetlenia
    }
    @GetMapping("/registration")                        // <- to wywołuje formularz rejestracji
    public String registration(Model model){            // model komunikuje back-end z szablonem Thymeleaf
        model.addAttribute("user", new User());       // przekazujemy do Thymelaf resolvera obiekt user
        return "registration";
    }
    @PostMapping("/registration")                       // <- to przekazywane są dane rejestracji
    public String registration(@ModelAttribute @Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            // poprawianie błędów
            return "registration";
        }
        // zapis do bazy
        System.out.println(user);
        return "index";
    }

}
