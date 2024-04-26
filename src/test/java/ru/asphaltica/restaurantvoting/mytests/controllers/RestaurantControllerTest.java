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
import ru.asphaltica.restaurantvoting.controller.RestaurantController;
import ru.asphaltica.restaurantvoting.to.RestaurantDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin@javaops.ru")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(value = {"/populate_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class RestaurantControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private RestaurantController restaurantController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void getAll() throws Exception {
        String expected = "[{\"id\":1,\"name\":\"Test restaurant 1\"},{\"id\":2,\"name\":\"Test restaurant 2\"}]";
        var response = this.mockMvc.perform(get("/api/restaurants"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resultJson = response.getResponse().getContentAsString();
        Assertions.assertEquals(expected, resultJson);
    }

    @Test
    @Order(2)
    void findById() throws Exception {
        this.mockMvc.perform(get("/api/restaurants/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test restaurant 2"));
    }

    @Test
    @Order(3)
    void findByIdFailed() throws Exception {
        this.mockMvc.perform(get("/api/restaurants/{id}", 9))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Restaurant with this id wasn't found"));
    }

    @Test
    @Order(4)
    void create() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        restaurant.setName("Макдональдс");
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @Order(5)
    void createFailedSameName() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        restaurant.setName("Макдональдс");
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(6)
    void createFailedLongName() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        restaurant.setName("Макдональдсfkdjhkgjvfhkjghfkjhgkjfdhgkjhfdjkghjfkgkjhfdjkghfjkhgkjfhjkghfjkghjfdkhgjh" +
                "dfbdfgbfjghjkfdghjkdfhgjkfhjkghfdjkhgkjfhgjkfdhgjkfdhjgkhfjkghjkfdhgjkfhgjkfhjkghfjhgjhdfjghjfdhgjfhgjhfhj" +
                "djhgfjhgjkfdhgjkhfdjkhgkjfhgjkfhjkghfkjghjkfhgjkfhkjghfjdkghjkfhgjkhfkjghfjkghjkfhgjkhfkjhgjkfhgj");
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(7)
    void createFailedNameIsBlank() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        mockMvc.perform(post("/api/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name - must not be empty;"));
    }

    @Test
    @Order(8)
    void deleteRestaurant() throws Exception {
        this.mockMvc.perform(delete("/api/restaurants/{id}", 3))
                .andDo(print())
                .andExpect(status().isNoContent());
        String expected = "[{\"id\":1,\"name\":\"Test restaurant 1\"},{\"id\":2,\"name\":\"Test restaurant 2\"}]";
        var response = this.mockMvc.perform(get("/api/restaurants"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resultJson = response.getResponse().getContentAsString();
        Assertions.assertEquals(expected, resultJson);
    }

    @Test
    @Order(9)
    void deleteRestaurantFailed() throws Exception {
        this.mockMvc.perform(delete("/api/restaurants/{id}", 100))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Restaurant with this id wasn't found"));
    }

    @Test
    @Order(10)
    void updateRestaurant() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        restaurant.setName("Restaurant updated!");
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        this.mockMvc.perform(put("/api/restaurants/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isNoContent());
        this.mockMvc.perform(get("/api/restaurants/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurant updated!"));
    }

    @Test
    @Order(11)
    void updateRestaurantFailed() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        restaurant.setName("Restaurant updated!");
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        this.mockMvc.perform(put("/api/restaurants/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Sql(value = {"/prepare_for_voting.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    @Order(12)
    void voting() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        mockMvc.perform(get("/api/restaurants"))
                .andDo(print());
        mockMvc.perform(get("/api/menus/today")).andDo(print());

        mockMvc.perform(get("/api/restaurants/available")).andDo(print());

        mockMvc.perform(post("/api/restaurants/{id}/vote", 2)).andDo(print());

        mockMvc.perform(get("/api/votes")).andDo(print());

        mockMvc.perform(get("/api/votes/voting_result")).andDo(print());


        //mockMvc.perform(get("/api/dishes")).andDo(print());
//        mockMvc.perform(post("/api/restaurants")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest)
//                ).andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("name - must not be empty;"));
    }








}
