package com.example.customer_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    @ManyToMany
    @JoinTable(
        name = "task_category",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
    @Type(type = "text")
    private String content;
    private Double price;
    private LocalDateTime publicationDate;
    private Boolean status;

    // metoda do dodawania kategorii do zbioru
    public void addCategory(Category category){
        this.categories.add(category);
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "task")
    private List<Basket> orders;

    public Task(Set<Category> categories, String content, Double price, User user) {
        this.categories = categories;
        this.content = content;
        this.price = price;
        this.user = user;
        this.status = true;
    }
}
