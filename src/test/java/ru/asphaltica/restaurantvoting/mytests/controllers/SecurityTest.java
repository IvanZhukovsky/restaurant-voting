package ru.asphaltica.restaurantvoting.mytests.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.asphaltica.restaurantvoting.controller.AdminController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private AdminController controller;

    @Autowired
    MockMvc mockMvc;

    @Test
    void accountRegisterAccessWithOutAuth() throws Exception{
        String jsonRequest = "{\"email\":\"user1@gmail.com\",\"firstName\":\"Tom\",\"lastName\":\"Baden\",\"password\":\"password\"}";
        this.mockMvc.perform(post("/api/account/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void test() throws Exception {
        this.mockMvc.perform(get("/api/votes/voting_result"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/api/menus/*/vote"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("admin@javaops.ru").password("admin"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
