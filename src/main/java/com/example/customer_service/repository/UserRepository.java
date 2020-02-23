package com.example.customer_service.repository;

import com.example.customer_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

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
    @Transactional
    @Modifying                                                      // executeUpdate
    @Query(value = "UPDATE user SET status = :status", nativeQuery = true)
    void changeStatusToUsers(@Param("status") Boolean status);
    // usuń wybraną role po role_name dla wszystkich użytkowników
    @Modifying
    @Query(value =
            "delete from user_role where role_id = (select role_id from role where role_name = :roleName)",
            nativeQuery = true)     // true -> składnia MySQL , false -> składnia JPQL
    void deleteAllRoleNamesFromUser(@Param("roleName") String roleName);

}
