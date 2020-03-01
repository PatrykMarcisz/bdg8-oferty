package com.example.customer_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketId;
    private LocalDate basketStart;
    private LocalDate basketDeadline;
    @ManyToOne()
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Basket(LocalDate basketStart, LocalDate basketDeadline, Task task, User user) {
        this.basketStart = basketStart;
        this.basketDeadline = basketDeadline;
        this.task = task;
        this.user = user;
    }
}
