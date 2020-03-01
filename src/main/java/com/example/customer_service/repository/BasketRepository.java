package com.example.customer_service.repository;

import com.example.customer_service.model.Basket;
import com.example.customer_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    // SELECT * FROM basket WHERE user_id = ?
    List<Basket> findAllByUser(User user);
}
