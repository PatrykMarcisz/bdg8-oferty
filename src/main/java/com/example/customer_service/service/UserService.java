package com.example.customer_service.service;

import com.example.customer_service.model.User;
import com.example.customer_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service            // klasa logiki biznesowej zarządzana w Spring Context
public class UserService {
    private UserRepository userRepository;
    @Autowired              // wstrzykiwanie zależności
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUserById(Long user_id){
        return userRepository.findById(user_id);
    }
}
