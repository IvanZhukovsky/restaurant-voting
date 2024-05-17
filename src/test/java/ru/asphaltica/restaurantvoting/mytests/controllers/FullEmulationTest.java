package ru.asphaltica.restaurantvoting.mytests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.asphaltica.restaurantvoting.controller.AdminController;
import ru.asphaltica.restaurantvoting.to.RestaurantDto;
import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin@javaops.ru")
@Sql(value = {"/prepare_for_full_emulation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FullEmulationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private AdminController controller;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ApplicationContext applicationContext;



    @SpyBean
    @Autowired
    private MenuRepository menuRepository;

    //Заходим в систему просматриваем всех пользователей
    @Order(1)
    @Test
    void getAllUsers() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Заходим как анонимный пользователь и пытаемся просмотреть всех пользователей
    @Order(2)
    @WithAnonymousUser
    @Test
    void getAllUsersFailedAnonimus() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    //Заходим под пользователем с правами USER и пытаемся просмотреть всех пользователей
    @Order(3)
    @WithUserDetails("user@gmail.com")
    @Test
    void getAllUsersFailedUser() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andDo(print());


    }

    //Заходим в систему как ADMIN и добавляем пользователя
    @Order(4)
    @Test
    void createUser() throws Exception {
        String jsonRequest = "{\"email\":\"izhukovsky@mail.ru\",\"firstName\":\"Ivan\",\"lastName\":\"Zhukovskiy\",\"password\":\"password\",\"roles\":[\"USER\"]}";
        var response = mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated());
        getAllUsers();
    }

    //Пользователь ADMIN создает 4 ресторана
    @Order(5)
    @Test
    void createRestaurants() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        restaurant.setName("Ресторан №1");
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andDo(print())
                .andExpect(status().isCreated());

        restaurant.setName("Ресторан №2");
        jsonRequest = objectMapper.writeValueAsString(restaurant);
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andDo(print())
                .andExpect(status().isCreated());

        restaurant.setName("Ресторан №3");
        jsonRequest = objectMapper.writeValueAsString(restaurant);
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andDo(print())
                .andExpect(status().isCreated());

        restaurant.setName("Рест №4");
        jsonRequest = objectMapper.writeValueAsString(restaurant);
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/restaurants"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Пользователь ADMIN обновляет 4 ресторан
    @Test
    @Order(6)
    void updateRestaurant() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        restaurant.setName("Ресторан №4");
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        this.mockMvc.perform(put("/api/restaurants/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isNoContent());
        this.mockMvc.perform(get("/api/restaurants"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Пользователь с правами USER пытается обновить ресторан
    @Order(7)
    @WithUserDetails("izhukovsky@mail.ru")
    @Test
    void updateRestaurantFailedUser() throws Exception {
        RestaurantDto restaurant = new RestaurantDto();
        restaurant.setName("Ресторан");
        String jsonRequest = objectMapper.writeValueAsString(restaurant);
        this.mockMvc.perform(put("/api/restaurants/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

//    //Пользователь ADMIN создает блюда для каждого ресторана
//    @Order(8)
//    @Test
//    void createDish() throws Exception {
//        //Добавляем еду в ресторан id=1
//        DishDto dishDTO = new DishDto();
//        dishDTO.setName("черный чай с сахаром");
//        dishDTO.setPrice(30.4);
//        addDishToRestaurant("1", objectMapper.writeValueAsString(dishDTO));
//
//        dishDTO.setName("суп гороховый");
//        dishDTO.setPrice(70.0);
//        addDishToRestaurant("1", objectMapper.writeValueAsString(dishDTO));
//
//        dishDTO.setName("салат мимоза");
//        dishDTO.setPrice(30.3);
//        addDishToRestaurant("1", objectMapper.writeValueAsString(dishDTO));
//
//        dishDTO.setName("чай с травами");
//        dishDTO.setPrice(60.5);
//        addDishToRestaurant("1", objectMapper.writeValueAsString(dishDTO));
//
//        //Добавляем еду в ресторан id=2
//        dishDTO.setName("хинкали с бараниной");
//        dishDTO.setPrice(300.4);
//        addDishToRestaurant("2", objectMapper.writeValueAsString(dishDTO));
//
//        dishDTO.setName("соус сацебели");
//        dishDTO.setPrice(75.0);
//        addDishToRestaurant("2", objectMapper.writeValueAsString(dishDTO));
//
//        dishDTO.setName("соус хачапури");
//        dishDTO.setPrice(37.3);
//        addDishToRestaurant("2", objectMapper.writeValueAsString(dishDTO));
//
//        dishDTO.setName("капучино");
//        dishDTO.setPrice(63.5);
//        addDishToRestaurant("2", objectMapper.writeValueAsString(dishDTO));
//
//
//        mockMvc.perform(get("/api/dishes")).andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    //Пользователь ADMIN создает МЕНЮ для каждого ресторана
//    @Order(9)
//    @Test
//    void createMenus() throws Exception {
//        Menu menu = new Menu();
//        Restaurant ownRestaurant = new Restaurant();
//        ownRestaurant.setId(1);
//        menu.setOwnRestaurant(ownRestaurant);
//        Dish dish1 = new Dish();
//        dish1.setId(1);
//        Dish dish2 = new Dish();
//        dish2.setId(2);
//        Dish dish3 = new Dish();
//        dish3.setId(3);
//        Dish dish4 = new Dish();
//        dish4.setId(4);
//        List<Dish> dishes = List.of(dish1, dish2, dish3, dish4);
//        menu.setDishes(dishes);
//        String jsonRequest = objectMapper.writeValueAsString(menu);
//        mockMvc.perform(post("/api/menus")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest)
//                ).andDo(print())
//                .andExpect(status().isCreated());
//
//        ownRestaurant.setId(2);
//        menu.setOwnRestaurant(ownRestaurant);
//        dish1 = new Dish();
//        dish1.setId(5);
//        dish2 = new Dish();
//        dish2.setId(6);
//        dish3 = new Dish();
//        dish3.setId(7);
//        dish4 = new Dish();
//        dish4.setId(8);
//        dishes = List.of(dish1, dish2, dish3, dish4);
//        menu.setDishes(dishes);
//        jsonRequest = objectMapper.writeValueAsString(menu);
//        mockMvc.perform(post("/api/menus")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest)
//                ).andDo(print())
//                .andExpect(status().isCreated());
//
//
//        mockMvc.perform(get("/api/menus"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//    }
//
//    //Анонимный пользователь регистрируется
//    @WithAnonymousUser
//    @Test
//    @Order(10)
//    void registerUser() throws Exception {
//        String jsonRequest = "{\"email\":\"new_user@gmail.com\",\"firstName\":\"Tom\",\"lastName\":\"Baden\",\"password\":\"password\"}";
//        this.mockMvc.perform(post("/api/account/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.email").value("new_user@gmail.com"));
//
//    }
//
//    //Новый пользователь логинится и просматривает свой аккаунт
//    @Test
//    @WithUserDetails("new_user@gmail.com")
//    @Order(11)
//    void getAccount() throws Exception {
//        this.mockMvc.perform(get("/api/account"))
//                .andDo(print())
//                .andExpect(authenticated())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("new_user@gmail.com"));
//    }

    //Новый пользователь запрашивает информацию о ресторанах доступных для голосования
    @Test
    @WithUserDetails("new_user@gmail.com")
    @Order(12)
    void getAvailableRestaurants() throws Exception {

        Long start = System.currentTimeMillis();
        mockMvc.perform(get("/api/restaurants/available")).andDo(print());
        Long end = System.currentTimeMillis();
        System.out.println("без кэша" + (end - start));

        start = System.currentTimeMillis();
        mockMvc.perform(get("/api/restaurants/available")).andDo(print());
        end = System.currentTimeMillis();
        System.out.println("c кэш" + (end - start));
        //verify(menuRepository, times(1)).findByCreateDateIsBetween(DateTimeUtil.atStartOfToday(), DateTimeUtil.atEndOfVoting());

        applicationContext.getBean("cacheManager");
    }



    //Новый пользователь запрашивает информацию по результатам голосования
    @Test
    @WithUserDetails("new_user@gmail.com")
    @Order(14)
    void getVotingResult() throws Exception {
        mockMvc.perform(get("/api/votes/voting_result")).andDo(print());
    }

    //Новый пользователь голосует за ресторан id=2
    @Test
    @WithUserDetails("new_user@gmail.com")
    @Order(15)
    void setVote1() throws Exception {
        mockMvc.perform(post("/api/restaurants/{id}/vote", 2)).andDo(print());
        mockMvc.perform(get("/api/votes/voting_result")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].voteCount").value(1));
    }

    //Новый пользователь голосует за ресторан id=2 повторно, результаты голосования не должны измениться
    @Test
    @WithUserDetails("new_user@gmail.com")
    @Order(16)
    void setVote2() throws Exception {
        mockMvc.perform(post("/api/restaurants/{id}/vote", 2)).andDo(print());
        mockMvc.perform(get("/api/votes/voting_result")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].voteCount").value(1))
                .andExpect(jsonPath("$[0].voteCount").value(0));
    }

    //Новый пользователь решил переголововать за ресторан id=1, результаты голосования должны измениться
    @Test
    @WithUserDetails("new_user@gmail.com")
    @Order(16)
    void setVote3() throws Exception {
        mockMvc.perform(post("/api/restaurants/{id}/vote", 1)).andDo(print());
        mockMvc.perform(get("/api/votes/voting_result")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].voteCount").value(0))
                .andExpect(jsonPath("$[0].voteCount").value(1));
    }



    //Новый пользователь запрашивает информацию по результатам голосования
    @Test
    @WithUserDetails("new_user@gmail.com")
    @Order(17)
    void getVotingResult1() throws Exception {
        mockMvc.perform(get("/api/votes/voting_result")).andDo(print());
    }

    private void addDishToRestaurant(String restaurantId, String jsonRequest) throws Exception {
        mockMvc.perform(post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .param("restaurantId", restaurantId)
                ).andDo(print())
                .andExpect(status().isCreated());
    }



}
