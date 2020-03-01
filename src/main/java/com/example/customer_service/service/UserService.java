package com.example.customer_service.service;

import com.example.customer_service.configuration.PasswdEncoder;
import com.example.customer_service.model.Role;
import com.example.customer_service.model.Task;
import com.example.customer_service.model.User;
import com.example.customer_service.repository.RoleRepository;
import com.example.customer_service.repository.TaskRepository;
import com.example.customer_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;

@Service            // klasa logiki biznesowej zarządzana w Spring Context
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TaskRepository taskRepository;
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.taskRepository = taskRepository;
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
            user.setRegistrationDate(LocalDateTime.now());      // domyślna data rejestracji
            user.setStatus(true);                               // domyśny status konta użytkownika
            if(user.getCompanyNip().equals("")){
                user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findFirstByRoleName("ROLE_USER"))));
            } else {
                user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findFirstByRoleName("ROLE_COMPANY"))));
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));   // szyfrowanie hasła BCrypt
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

    public void updateUser(User user) {     // obiekt przekazany z formularza
        Optional<User> userOpt = userRepository.findById(user.getUserId());
        if (userOpt.isPresent()) {
            User updatedUser = userOpt.get();   // oryginalny obiekt z DB
            updatedUser.setName(user.getName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setCompanyName(user.getCompanyName());
            updatedUser.setCompanyAddress(user.getCompanyAddress());
            updatedUser.setCompanyNip(user.getCompanyNip());
            userRepository.save(updatedUser);               // aktualizacja danych
        }
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
        roleRepository.deleteAllRoleNamesFromUser(roleName);
    }

    public User getUserByEmail(String loggedEmail){
        return userRepository.findUserByEmail(loggedEmail);
    }

    public Boolean hasRole(Authentication auth, String roleName){
        if(auth == null){
            return false;
        }
        UserDetails principal = (UserDetails) auth.getPrincipal();
        return principal.getAuthorities().stream().anyMatch(o -> ((GrantedAuthority) o).getAuthority().equals(roleName));
    }
}



