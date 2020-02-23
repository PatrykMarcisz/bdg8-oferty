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
                            @RequestParam String password, String companyName, String companyAddress, String nip){
        if(nip == null){
        return userService.register(
                new User(name, lastName, email, password, LocalDateTime.now(), true, new ArrayList<>())
            );
        }
        return userService.register(
                new User(name, lastName, email, password, LocalDateTime.now(), true, new ArrayList<>(),
                companyName, companyAddress, nip)
        );
    }
    @DeleteMapping("/delete")
    public Boolean deleteUserByEmail(@RequestParam String userEmail){
        return userService.deleteUser(userEmail);
    }
    @PutMapping("/updateUserStatus")
    public Boolean updateStatus(
            @RequestParam("user_id") Long userId,
            @RequestParam("status") Boolean status){
        return userService.updateStatus(userId, status);
    }
    @PutMapping("/updateUserPassword")
    public Boolean updatePassword(
            @RequestParam("user_id") Long userId,
            @RequestParam("password1") String password1,
            @RequestParam("password2") String password2){
        return userService.updatePassword(userId, password1, password2);
    }
    @PutMapping("/updateUser")
    public Boolean updataUserById(
            @RequestParam("user_id") Long userId,
            String name, String lastName, String email, String password, String companyName, String companyAddress, String companyNip
    ){
        return userService.updateUserById(userId, name, lastName,
                email,password,companyName,companyAddress, companyNip);
    }
    @PostMapping("/addRole")
    public Boolean addRoleToUser(@RequestParam("user_id") Long userId, @RequestParam String roleName){
        return userService.addRoleToUser(roleName, userId);
    }
    @DeleteMapping("/removeRole")
    public Boolean removeRoleToUser(@RequestParam("user_id") Long userId, @RequestParam String roleName){
        return userService.removeRoleFromUser(roleName, userId);
    }
}
