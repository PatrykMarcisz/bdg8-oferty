package com.example.customer_service;

import com.example.customer_service.model.Role;
import com.example.customer_service.model.User;
import com.example.customer_service.repository.RoleRepository;
import com.example.customer_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

@EnableSwagger2
@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init (UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        return args -> {
            User user = new User();
            user.setCompanyAddress("Krakow");
            user.setCompanyName("Patryk Marcisz");
            user.setCompanyNip("222222");
            user.setEmail("patryk.marcisz@gmail.com");
            user.setLastName("Marcisz");
            user.setName("Patryk");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRegistrationDate(LocalDateTime.now());
            user.setStatus(true);
            user.setRoles(new HashSet<>(roleRepository.findAll()));
            userRepository.save(user);
        };
    }

}
