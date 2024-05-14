package ru.asphaltica.restaurantvoting.mytests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.asphaltica.restaurantvoting.controller.AdminController;
import ru.asphaltica.restaurantvoting.model.User;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.asphaltica.restaurantvoting.UserTestData.ADMIN_MAIL;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin@javaops.ru")
@Sql(value = {"/populate_test_data.sql"}, executionPhase =  Sql.ExecutionPhase.BEFORE_TEST_CLASS)

class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private AdminController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll() throws Exception {
        var response = this.mockMvc.perform(get("/api/admin/users"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(response.getResponse().getContentAsString());
    }

    @Test
    void findById() throws Exception {
        var response = this.mockMvc.perform(get("/api/admin/users/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(response.getResponse().getContentAsString());
    }

    @Test
    void findByIdFailed() throws Exception {
        var response = this.mockMvc.perform(get("/api/admin/users/{id}", 56))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with this id wasn't found"));

    }

    @Test
    void findByEmail() throws Exception {
        this.mockMvc.perform(get("/api/admin/users/by").param("email", "admin@javaops.ru"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findByEmailFailedBadFormat() throws Exception {
        this.mockMvc.perform(get("/api/admin/users/by").param("email", "adminjavaops.ru"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByEmailFailedBlank() throws Exception {
        this.mockMvc.perform(get("/api/admin/users/by").param("email", ""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create() throws Exception {
        String jsonRequest = "{\"email\":\"izhukovsky@mail.ru\",\"firstName\":\"Ivan\",\"lastName\":\"Zhukovskiy\",\"password\":\"password\",\"roles\":[\"USER\"]}";
        var response = this.mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createFailedUserExist() throws Exception {
        String jsonRequest = "{\"email\":\"admin@javaops.ru\",\"firstName\":\"Ivan\",\"lastName\":\"Zhukovskiy\",\"password\":\"password\",\"roles\":[\"USER\"]}";
        var response = this.mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFailedBadEmailFormat() throws Exception {
        String jsonRequest = "{\"email\":\"izhukovskymail.ru\",\"firstName\":\"Ivan\",\"lastName\":\"Zhukovskiy\",\"password\":\"password\",\"roles\":[\"USER\"]}";
        var response = this.mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email - format must match email address;"));
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/api/admin/users/{id}", 3))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserFailedNotFound() throws Exception {
        this.mockMvc.perform(delete("/api/admin/users/{id}", 56))
                .andDo(print())
                .andExpect(status().isNotFound());
    }





}


