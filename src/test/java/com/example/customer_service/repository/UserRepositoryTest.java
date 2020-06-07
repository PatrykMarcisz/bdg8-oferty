package com.example.customer_service.repository;

import com.example.customer_service.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
//    @Sql(statements = {"INSERT INTO user (user_id, name, last_name, email, password) VALUES(1, 'Adam', 'Malysz', 'adam@op.pl', 'qwe123')"})
    @Sql(value = "/users.sql")
    void shouldFindUserByEmail() {
        //given
        String email = "adam@op.pl";
        //when
        User user=userRepository.findUserByEmail(email);
        //then
        assertEquals(email, user.getEmail());
        assertEquals("Adam", user.getName());
        assertEquals("Malysz", user.getLastName());
    }


}