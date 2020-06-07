package com.example.customer_service.controller;

import com.example.customer_service.model.User;
import com.example.customer_service.service.TaskService;
import com.example.customer_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;

@Controller
public class UserFrontEndController {

    private UserService userService;
    private TaskService taskService;

    @Autowired
    public UserFrontEndController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/login")
    public String login(Authentication auth, Model model){
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        return "login";
    }

    @GetMapping("/")
    public String index(Authentication auth, Model model){
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("tasks", taskService.getAllTasksOrderByPublicationDateDesc());
        model.addAttribute("isAdmin", userService.hasRole(auth, "ROLE_ADMIN"));         // do sprawdzania uprawnień R_A
        model.addAttribute("isCompany", userService.hasRole(auth, "ROLE_COMPANY"));     // do sprawdzania uprawnień R_C
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        return "index";     // nazwa widoku .html do wyświetlenia
    }

    @GetMapping("/registration")                        // <- to wywołuje formularz rejestracji
    public String registration(Authentication auth, Model model){            // model komunikuje back-end z szablonem Thymeleaf
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("user", new User());       // przekazujemy do Thymelaf resolvera obiekt user
        return "registration";
    }

    @PostMapping("/registration")                       // <- to przekazywane są dane rejestracji
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model,
                               Authentication auth){
        if(bindingResult.hasErrors()){
            // poprawianie błędów
            return "registration";
        }
        // zapis do bazy
        boolean isUniqueEmail = userService.register(user);
        if(!isUniqueEmail){
            model.addAttribute("isUniqueEmail", "adres "+user.getEmail()+" już istnieje w bazie danych");
            return "registration";
        }
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("tasks", taskService.getAllTasksOrderByPublicationDateDesc());
        model.addAttribute("isAdmin", userService.hasRole(auth, "ROLE_ADMIN"));         // do sprawdzania uprawnień R_A
        model.addAttribute("isCompany", userService.hasRole(auth, "ROLE_COMPANY"));     // do sprawdzania uprawnień R_C
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");
        return "index";
    }

    @GetMapping("/userProfile")
    public String userProfile(
            Authentication auth,
            Model model){
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("tasks", taskService.getAllTasksOrderByPublicationDateDesc());
        model.addAttribute("isAdmin", userService.hasRole(auth, "ROLE_ADMIN"));         // do sprawdzania uprawnień R_A
        model.addAttribute("isCompany", userService.hasRole(auth, "ROLE_COMPANY"));     // do sprawdzania uprawnień R_C
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");
        model.addAttribute("user", auth != null ? userService.getUserByEmail(((UserDetails)auth.getPrincipal()).getUsername()) : "");
        model.addAttribute("roleList",
                auth != null ?
                        new ArrayList<>(userService.getUserByEmail(((UserDetails)auth.getPrincipal()).getUsername()).getRoles()) :
                        new ArrayList<>());
        model.addAttribute("baskets", taskService.getBasketsForUser(auth));
        return "userProfile";
    }

    @PostMapping("/updateUserData")
    public String updateUserData(
            @ModelAttribute @Valid User user, BindingResult bindingResult,
            Authentication auth,
            Model model){
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("tasks", taskService.getAllTasksOrderByPublicationDateDesc());
        model.addAttribute("isAdmin", userService.hasRole(auth, "ROLE_ADMIN"));         // do sprawdzania uprawnień R_A
        model.addAttribute("isCompany", userService.hasRole(auth, "ROLE_COMPANY"));     // do sprawdzania uprawnień R_C
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails)auth.getPrincipal()).getUsername() : "");
        model.addAttribute("user", auth != null ? userService.getUserByEmail(((UserDetails)auth.getPrincipal()).getUsername()) : "");
        model.addAttribute("roleList",
                auth != null ?
                        new ArrayList<>(userService.getUserByEmail(((UserDetails)auth.getPrincipal()).getUsername()).getRoles()) :
                        new ArrayList<>());
        model.addAttribute("baskets", taskService.getBasketsForUser(auth));
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> System.out.println(objectError.getDefaultMessage()));
            return "userProfile";
        }
        // aktualizacja
        userService.updateUser(user);
        return "redirect:/userProfile";
    }

}
