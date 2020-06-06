package com.example.customer_service.controller;

import com.example.customer_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorsController implements ErrorController {

    private final UserService userService;

    @Autowired
    public ErrorsController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    public String errorPage(Model model, Authentication auth){
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("isAdmin", userService.hasRole(auth, "ROLE_ADMIN"));         // do sprawdzania uprawnień R_A
        model.addAttribute("isCompany", userService.hasRole(auth, "ROLE_COMPANY"));     // do sprawdzania uprawnień R_C
        return "errorPage";
    }

    @GetMapping("/login_error")
    public String loginErrorPage(Model model, Authentication auth){
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("isAdmin", userService.hasRole(auth, "ROLE_ADMIN"));         // do sprawdzania uprawnień R_A
        model.addAttribute("isCompany", userService.hasRole(auth, "ROLE_COMPANY"));     // do sprawdzania uprawnień R_C
        model.addAttribute("info", "Błąd logowania");       // do osadzenia w errorPage
        return "errorPage";
    }
}
