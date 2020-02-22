package com.example.customer_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Column(name = "user_id")   // nadpisanie nazwy mapowane w tabeli DB
    @Id                         // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String lastName;
    private String email;
    private String password;
    @ManyToMany         // relacja wiele do wielu
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    private LocalDateTime registrationDate;
    private Boolean status;
    
    @OneToMany(
        mappedBy = "user"
    )
    private List<Task> tasks;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
