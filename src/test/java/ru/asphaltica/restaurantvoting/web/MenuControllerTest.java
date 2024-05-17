package ru.asphaltica.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.asphaltica.restaurantvoting.AbstractControllerTest;
import ru.asphaltica.restaurantvoting.MenuTestData;
import ru.asphaltica.restaurantvoting.RestaurantTestData;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.util.JsonUtil;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.asphaltica.restaurantvoting.MenuTestData.*;
import static ru.asphaltica.restaurantvoting.UserTestData.ADMIN_MAIL;
import static ru.asphaltica.restaurantvoting.UserTestData.USER_MAIL;
import static ru.asphaltica.restaurantvoting.controller.MenuController.REST_URL;
import static ru.asphaltica.restaurantvoting.validation.UniqueDishesNamesValidator.*;

class MenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MenuRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER_WITHOUT_DISHES.contentJson(menu1, menu2, menu3, menu4));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-restaurant").param("restaurantId", String.valueOf(RestaurantTestData.RESTAURANT1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER_WITHOUT_RESTAURANT.contentJson(menu1, menu2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByRestaurantIdInvalid() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-restaurant").param("restaurantId", ""))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByRestaurantIdNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-restaurant").param("restaurantId", String.valueOf(RestaurantTestData.NOT_FOUND_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MenuTestData.MENU_MATCHER.contentJson(menu1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @WithUserDetails(value = USER_MAIL)
    @Test
    void getAccessDenied() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU1_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + MENU1_ID))
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(MENU1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        Menu newMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andDo(print());

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(repository.findByIdWithRestaurantAndDishes(newId).get(), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithDuplicateDate() throws Exception {
        Menu menu = getWithDublicateDate();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menu)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithDuplicateDishes() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_WITH_DUPBLICATE_DISHES))
                .andDo(print())
                .andExpect(status().isConflict());


    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithDuplicateNamesOfDishes() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_WITH_DUPBLICATE_NAMES_OF_DISHES))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_DISHES_NAMES)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Menu invalid = new Menu(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + MENU2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(repository.findByIdWithRestaurantAndDishes(MENU2_ID).get(), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Menu invalid = new Menu(null, null, null, null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + MENU2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

}
