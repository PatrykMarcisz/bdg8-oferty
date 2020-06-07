package com.example.customer_service.service;

import com.example.customer_service.model.Role;
import com.example.customer_service.model.User;
import com.example.customer_service.repository.RoleRepository;
import com.example.customer_service.repository.TaskRepository;
import com.example.customer_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

//    @BeforeEach
//    void createUSerService() {
//        userService = new UserService(userRepository,roleRepository,taskRepository);
//    }

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldReturnAllUsers() {
        //given
        User uSer1 = new User();
        User uSer2 = new User();
        User uSer3 = new User();
        List<User>users = Arrays.asList(uSer1,uSer2, uSer3);
        when(userRepository.findAll()).thenReturn(users);
        //when
        List<User> allUSers = userService.getAllUsers();
        //then
        assertEquals(3,allUSers.size());
        assertTrue(allUSers.containsAll(users));
    }

    @Test
    void shouldReturnNewPassword(){
        //given
        User user = new User();
        user.setPassword("qwe123");

        //when
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.encode("123")).thenReturn("#123#");
        boolean update=userService.updatePassword(1L, "123", "123");

        //given
        assertTrue(update);
        assertEquals("#123#", user.getPassword());
    }

    @Test
    void shouldReturnFalseWhenDifferentPasswords(){
        //when
        boolean update=userService.updatePassword(1L, "123", "1234");

        //given
        assertFalse(update);

        verify(userRepository,never()).findById(any());
    }

    @Test
    void shouldNotRegisterNewUserWhenExist(){
        //given
        User user = new User();
        user.setEmail("123@op.pl");

        //when
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        boolean register = userService.register(user);

        //given
        assertFalse(register);
    }

    @Test
    void shouldRegisterWithoutNip(){
        //given
        User user = new User();
        user.setCompanyNip("");
        user.setPassword("123");

        Role role = new Role(1L, "ROLE_USER");
        //when
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(roleRepository.findFirstByRoleName("ROLE_USER"))
                .thenReturn(role);
        when(passwordEncoder.encode("123")).thenReturn("#123#");
        //given
        boolean register = userService.register(user);

        //given
        assertTrue(register);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User user1=userArgumentCaptor.getValue();

        assertTrue(user.getRoles().contains(role));
        assertEquals(user.getPassword(), "#123#");

    }

    @Test
    void shouldRegisterAsCompany(){
        //given
        User user = new User();
        user.setCompanyNip("123456789");
        user.setPassword("123");

        Role role = new Role(1L, "ROLE_COMPANY");
        //when
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(roleRepository.findFirstByRoleName("ROLE_COMPANY"))
                .thenReturn(role);
        when(passwordEncoder.encode("123")).thenReturn("#123#");
        //given
        boolean register = userService.register(user);

        //given
        assertTrue(register);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User user1=userArgumentCaptor.getValue();

        assertTrue(user.getRoles().contains(role));
        assertEquals(user.getPassword(), "#123#");

    }


}