package ru.asphaltica.restaurantvoting.mytests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.asphaltica.restaurantvoting.controller.DishController;
import ru.asphaltica.restaurantvoting.to.DishDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin@javaops.ru")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(value = {"/populate_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class DishControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DishController dishController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void getAll() throws Exception {
        String expected = "[" +
                "{\"id\":1,\"name\":\"черный чай с сахаром\",\"price\":10.5,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}" +
                ",{\"id\":2,\"name\":\"суп гороховый\",\"price\":70.0,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}" +
                ",{\"id\":3,\"name\":\"салат мимоза\",\"price\":30.3,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}," +
                "{\"id\":4,\"name\":\"чай с травами\",\"price\":60.5,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}]";
        var response = this.mockMvc.perform(get("/api/dishes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resultJson = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        //Assertions.assertEquals(expected, resultJson);
    }

    @Test
    @Order(2)
    void findById() throws Exception {
        String expected = "{\"id\":2,\"name\":\"суп гороховый\",\"price\":70.0,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}";
        var respose = this.mockMvc.perform(get("/api/dishes/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resultJson = respose.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, resultJson);
    }

    @Test
    @Order(3)
    void findByIdFailed() throws Exception {
        this.mockMvc.perform(get("/api/dishes/{id}", 100))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void create() throws Exception {
        DishDto dishDTO = new DishDto();
        dishDTO.setName("Test dish Name");
        dishDTO.setPrice(1000.5);
        String jsonRequest = objectMapper.writeValueAsString(dishDTO);
        mockMvc.perform(post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .param("restaurantId", "1")
                ).andDo(print())
                .andExpect(status().isCreated());

        String expected = "{\"id\":5,\"name\":\"Test dish Name\",\"price\":1000.5,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}";
        var respose = this.mockMvc.perform(get("/api/dishes/{id}", 5))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resultJson = respose.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, resultJson);
    }

    @Test
    @Order(5)
    void createFailedSameName() throws Exception {
        DishDto dishDTO = new DishDto();
        dishDTO.setName("Test dish Name");
        dishDTO.setPrice(1000.5);
        String jsonRequest = objectMapper.writeValueAsString(dishDTO);
        mockMvc.perform(post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .param("rest_id", "1")
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    void createFailedToAnotherRestaurant () throws Exception {
        DishDto dishDTO = new DishDto();
        dishDTO.setName("Test dish Name");
        dishDTO.setPrice(1000.5);
        String jsonRequest = objectMapper.writeValueAsString(dishDTO);
        mockMvc.perform(post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .param("rest_id", "2")
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void createFailedNameIsBlank() throws Exception {
        DishDto dishDTO = new DishDto();
        dishDTO.setName("dfdg");
        //dishDTO.setPrice(1000.5);
        String jsonRequest = objectMapper.writeValueAsString(dishDTO);
        mockMvc.perform(post("/api/dishes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                        .param("rest_id", "1")
                ).andDo(print())
                .andExpect(status().isBadRequest());
                //.andExpect(jsonPath("$.message").value("name - must not be empty;"));
    }

    @Test
    @Order(8)
    void deleteDish() throws Exception {
        this.mockMvc.perform(delete("/api/dishes/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());
        String expected = "[{\"id\":2,\"name\":\"суп гороховый\",\"price\":70.0,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}," +
                "{\"id\":3,\"name\":\"салат мимоза\",\"price\":30.3,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}," +
                "{\"id\":4,\"name\":\"чай с травами\",\"price\":60.5,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}," +
                "{\"id\":5,\"name\":\"Test dish Name\",\"price\":1000.5,\"ownRestaurant\":{\"id\":1,\"name\":\"Test restaurant 1\"}}]";
        var response = this.mockMvc.perform(get("/api/dishes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resultJson = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, resultJson);
    }

    @Test
    @Order(9)
    void deleteDishFailed() throws Exception {
        this.mockMvc.perform(delete("/api/dishes/{id}", 100))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Dish with this id wasn't found"));
    }

    @Test
    @Order(10)
    void updateRestaurant() throws Exception {
        DishDto dishDTO = new DishDto();
        //dishDTO.setRestaurant(2);
        dishDTO.setName("Updated test dish Name");
        dishDTO.setPrice(777.0);
        String jsonRequest = objectMapper.writeValueAsString(dishDTO);
        this.mockMvc.perform(put("/api/dishes/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isNoContent());
        this.mockMvc.perform(get("/api/dishes/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated test dish Name"));
    }

    @Test
    @Order(11)
    void updateRestaurantFailed() throws Exception {
        DishDto dishDTO = new DishDto();
        //dishDTO.setRestaurant(2);
        dishDTO.setName("Updated test dish Name");
        dishDTO.setPrice(777.0);
        String jsonRequest = objectMapper.writeValueAsString(dishDTO);
        this.mockMvc.perform(put("/api/dishes/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
