package com.example.customer_service.controller;

import com.example.customer_service.model.User;
import com.example.customer_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.*;

@RestController   // mapuje żądania http i zwraca Obiekt API
//@Controller     // mapuje żądania http i zwraca nazwę widoku gdzie są przekazywane Obiekty
public class UserController {
    private UserService userService;
    // wstrzykiwanie zależności przez konstruktor
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/users/id={user_id}")      // {część zmienna ścieżki}
    public User getUserById(@PathVariable("user_id") Long user_id){
        Optional<User> userOpt = userService.getUserById(user_id);
        return userOpt.orElseGet(User::new);
    }
    @PostMapping("/register")
    public Boolean register(@RequestParam String name, @RequestParam String lastName, @RequestParam String email,
                            @RequestParam String password, @RequestParam String companyName,
                            @RequestParam String companyAddress, @RequestParam String nip){
        if(nip.equals("")){
        return userService.register(
                new User(name, lastName, email, password, LocalDateTime.now(), true, new ArrayList<>())
            );
        }
        return userService.register(
                new User(name, lastName, email, password, LocalDateTime.now(), true, new ArrayList<>(),
                companyName, companyAddress, nip)
        );
    }
}