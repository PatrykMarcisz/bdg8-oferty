package com.example.customer_service.controller;

import com.example.customer_service.model.User;
import com.example.customer_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
}
