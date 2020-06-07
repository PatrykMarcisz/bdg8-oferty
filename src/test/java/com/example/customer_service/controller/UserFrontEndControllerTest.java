package com.example.customer_service.controller;

import com.example.customer_service.model.User;
import com.example.customer_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(UserFrontEndController.class)
@SpringBootTest
@ActiveProfiles("test")
class UserFrontEndControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    /* nie dziala :(
    @Test
    @WithMockUser(username = "patryk.marcisz@gmail.com", password = "123456")
    void shouldShowUserProfile() throws Exception {

        User user = new User();
        user.setEmail("patryk.marcisz@gmail.com");
        user.setRoles(Collections.emptySet());

        when(userService.hasRole(any(), eq("ROLE_ADMIN")))
                .thenReturn(true);
        when(userService.hasRole(any(), eq("ROLE_COMPANY")))
                .thenReturn(true);
        when(userService.getUserByEmail("patryk.marcisz@gmail.com"))
                .thenReturn(user);
        when(userService.getUserByEmail("patryk.marcisz@gmail.com"))
                .thenReturn(user);

//        when(userService.hasRole())
        mockMvc.perform(get("/userProfile"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("loggedEmail", equalTo("patryk.marcisz@gmail.com")))
                .andExpect(model().attribute("isLogged", equalTo(true)))
                .andExpect(model().attribute("tasks", equalTo(Collections.emptyList())))
                .andExpect(view().name("userProfile"));
    }
    */

    @Test
    @WithMockUser(username = "patryk.marcisz@gmail.com", password = "123456")
    void shouldForwardToUserProfileWhenNotAllUserDataProvided() throws Exception {

        User user = new User();
        user.setEmail("patryk.marcisz@gmail.com");
        user.setRoles(Collections.emptySet());

        when(userService.hasRole(any(), eq("ROLE_ADMIN")))
                .thenReturn(true);
        when(userService.hasRole(any(), eq("ROLE_COMPANY")))
                .thenReturn(true);
        when(userService.getUserByEmail("patryk.marcisz@gmail.com"))
                .thenReturn(user);
        when(userService.getUserByEmail("patryk.marcisz@gmail.com"))
                .thenReturn(user);

        mockMvc.perform(post("/updateUserData")
                .param("id", "123456")
                .param("action", "save")
                .flashAttr("user", user)
                .flashAttr("action", "123456"))
                .andExpect(view().name("userProfile"));

        verify(userService, never()).updateUser(user);

    }

}