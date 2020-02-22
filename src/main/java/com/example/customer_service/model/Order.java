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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDate orderStart;
    private LocalDate orderDeadline;
    @ManyToOne()
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
