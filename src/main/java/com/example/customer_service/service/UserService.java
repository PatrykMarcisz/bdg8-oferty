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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long user_id) {
        return userRepository.findById(user_id);
    }

    public Boolean register(User user) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            return false;
        } else {
            userRepository.save(user);      // INSERT INTO USER
            return true;
        }
    }
    public Boolean deleteUser(String userEmail){
        User user = userRepository.findUserByEmail(userEmail);
        if(user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }
    public Boolean updateStatus(Long userId, Boolean status){
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()){
            User user = userOpt.get();
            user.setStatus(status);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
