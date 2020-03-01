package com.example.customer_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
//@Data
public class User {

    @Column(name = "user_id")   // nadpisanie nazwy mapowane w tabeli DB
    @Id                         // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank(message = "pole obowiązkowe")
    @Size(min = 3, max = 255, message = "imię musi zawierać od {min} do {max} znaków")
    private String name;
    @NotBlank(message = "pole obowiązkowe")
    @Size(min = 3, max = 255, message = "nazwisko musi zawierać od {min} do {max} znaków")
    private String lastName;
    @NotBlank(message = "pole obowiązkowe")
    @Email(message = "niepoprawny adres e-mail")
    private String email;
//    @Pattern(regexp = "...")
    @NotBlank(message = "pole obowiązkowe")
    @Size(min = 6, max = 255, message = "hasło musi zawierać od {min} do {max} znaków")
    private String password;

    private String companyName;
    private String companyAddress;
//    @Size(min = 10, max = 10, message = "nip musi zawierać od {min} do {max} znaków")
    private String companyNip;

                        // FetchType.Eager -> pobiera automatycznie
                                                 // powiązania z tabelką Role
    @ManyToMany(fetch = FetchType.EAGER)         // relacja wiele do wielu
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    private LocalDateTime registrationDate;
    private Boolean status;

    @OneToMany(mappedBy = "user")
    private List<Basket> baskets;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;


    // konstruktor user
    public User(String name, String lastName, String email, String password, LocalDateTime registrationDate, Boolean status, List<Basket> baskets) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.status = status;
        this.baskets = baskets;
    }
    // kontruktor company
    public User(String name, String lastName, String email, String password, LocalDateTime registrationDate, Boolean status, List<Task> tasks, String companyName, String companyAddress, String companyNip) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.status = status;
        this.tasks = tasks;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyNip = companyNip;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyNip() {
        return companyNip;
    }

    public void setCompanyNip(String companyNip) {
        this.companyNip = companyNip;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
