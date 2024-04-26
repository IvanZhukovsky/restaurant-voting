package ru.asphaltica.restaurantvoting.mytests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.asphaltica.restaurantvoting.controller.AdminController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin@javaops.ru")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(value = {"/populate_test_data.sql"}, executionPhase =  Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private AdminController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void getUser() throws Exception {
        this.mockMvc.perform(get("/api/account"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@javaops.ru"));
    }



    @WithAnonymousUser
    @Test
    @Order(3)
    void registerUser() throws Exception {
        String jsonRequest = "{\"email\":\"user1@gmail.com\",\"firstName\":\"Tom\",\"lastName\":\"Baden\",\"password\":\"password\"}";
        this.mockMvc.perform(post("/api/account/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user1@gmail.com"));
    }

    @WithAnonymousUser
    @Test
    @Order(4)
    void registerUserFailedExist() throws Exception {
        String jsonRequest = "{\"email\":\"user@gmail.com\",\"firstName\":\"Tom\",\"lastName\":\"Baden\",\"password\":\"password\"}";
        this.mockMvc.perform(post("/api/account/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email - a user with this email is already registered;"));
    }

    @WithAnonymousUser
    @Test
    @Order(5)
    void registerUserFailedBadEmailFormat() throws Exception {
        String jsonRequest = "{\"email\":\"user1gmail.com\",\"firstName\":\"Tom\",\"lastName\":\"Baden\",\"password\":\"password\"}";
        this.mockMvc.perform(post("/api/account/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email - format must match email address;"));
    }

    @WithAnonymousUser
    @Test
    @Order(6)
    void registerUserFailedEmailBlanc() throws Exception {
        String jsonRequest = "{\"firstName\":\"Tom\",\"lastName\":\"Baden\",\"password\":\"password\"}";
        this.mockMvc.perform(post("/api/account/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email - email address must be specified;"));
    }

    @Test
    @Order(7)
    void updateUserPasswordExclude() throws Exception {
        String jsonRequest = "{\"email\":\"admin@javaops.ru\",\"firstName\":\"Josef\",\"lastName\":\"Baden\"}";
        this.mockMvc.perform(put("/api/account").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isNoContent());

        this.mockMvc.perform(formLogin().user("admin@javaops.ru").password("admin"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Order(7)
    void updateUserPasswordInclude() throws Exception {
        String jsonRequest = "{\"email\":\"admin@javaops.ru\",\"firstName\":\"Josef\",\"lastName\":\"Baden\", \"password\":\"Baden\"}";
        this.mockMvc.perform(put("/api/account").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isNoContent());
        //Проверка запрета на вход по старому паролю
        this.mockMvc.perform(formLogin().user("admin@javaops.ru").password("admin"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
        //Проверка работы нового пароля
        this.mockMvc.perform(formLogin().user("admin@javaops.ru").password("Baden"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Order(8)
    void getUpdatedUser() throws Exception {
        this.mockMvc.perform(get("/api/account"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Josef"))
                .andExpect(jsonPath("$.lastName").value("Baden"));
    }

    @Test
    @Order(9)
    void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/api/account"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isNoContent());
    }








}
