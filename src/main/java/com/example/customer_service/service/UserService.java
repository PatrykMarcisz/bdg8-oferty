package com.example.customer_service.service;

import com.example.customer_service.model.Role;
import com.example.customer_service.model.User;
import com.example.customer_service.repository.RoleRepository;
import com.example.customer_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service            // klasa logiki biznesowej zarządzana w Spring Context
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    public Boolean deleteUser(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public Boolean updateStatus(Long userId, Boolean status) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(status);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Boolean updatePassword(Long userId, String password1, String password2) {
        if (password1.equals(password2)) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPassword(password1);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public Boolean updateUserById(Long userId, String name, String lastName,
                                  String email, String password, String companyName, String companyAddress, String companyNip) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if(userRepository.findUserByEmail(email) == null) {
                user.setName(name != null ? name : user.getName());
                user.setLastName(lastName != null ? lastName : user.getLastName());
                user.setEmail(email != null ? email : user.getEmail());
                user.setPassword(password != null ? password : user.getPassword());
                user.setCompanyName(companyName != null && user.getCompanyName() != null ? companyName : user.getCompanyName());
                user.setCompanyAddress(companyAddress != null && user.getCompanyAddress() != null ? companyAddress : user.getCompanyAddress());
                user.setCompanyNip(companyNip != null && user.getCompanyNip() != null ? companyNip : user.getCompanyNip());
                userRepository.save(user);      // UPDATE USER SET ...
                return true;
            }
            return false;       // jest użytkownik o zadanym user_id ale podajesz istniejący w bazie email
        }
        return false;           // nie ma takiego użytkownika w badzie po user_id
    }
    public Boolean addRoleToUser(String roleName, Long userId){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            Role role = roleRepository.findFirstByRoleName(roleName);
            if (role == null) return false;
            Set<Role> roles = user.getRoles();  // zbiór ról użytkownika
            roles.add(role);                    // dodanie nowej roli dla użytkownika
            user.setRoles(roles);               // aktualizacja zbioru ról
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public Boolean removeRoleFromUser(String roleName, Long userId){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            Role role = roleRepository.findFirstByRoleName(roleName);
            if (role == null) return false;
            Set<Role> roles = user.getRoles();
            roles.remove(role);
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public void findAllEmailAndRoleName(){
        userRepository.findAllEmailAndRoleName().forEach(o -> System.out.println(o[0] + " : " + o[1]));
    }
    public void changeStatusToUsers(Boolean status){
        userRepository.changeStatusToUsers(status);
    }
    public void deleteAllRoleNamesFromUser(String roleName){
        userRepository.deleteAllRoleNamesFromUser(roleName);
    }
}



