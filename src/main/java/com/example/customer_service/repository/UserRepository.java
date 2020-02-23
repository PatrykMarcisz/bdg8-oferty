package com.example.customer_service.repository;

import com.example.customer_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findUserByEmail(String email);

    @Query(
        value = "SELECT email, role_name FROM user JOIN user_role " +
            "ON (user.user_id = user_role.user_id) JOIN role " +
            "ON (role.role_id = user_role.role_id)",
        nativeQuery = true
    )
    List<Object[]> findAllEmailAndRoleName();
    // aktywuj wszystkich użytkowników adnotacją Query

    // usuń wybranego po id użytkownika adnotacją Query
}
