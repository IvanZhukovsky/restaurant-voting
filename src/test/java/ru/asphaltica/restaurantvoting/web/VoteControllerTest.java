package ru.asphaltica.restaurantvoting.web;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.asphaltica.restaurantvoting.AbstractControllerTest;
import ru.asphaltica.restaurantvoting.controller.VoteController;
import ru.asphaltica.restaurantvoting.model.Vote;
import ru.asphaltica.restaurantvoting.repository.VoteRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.asphaltica.restaurantvoting.RestaurantTestData.RESTAURANT1_ID;
import static ru.asphaltica.restaurantvoting.RestaurantTestData.RESTAURANT2_ID;
import static ru.asphaltica.restaurantvoting.UserTestData.*;
import static ru.asphaltica.restaurantvoting.VoteTestData.*;
import static ru.asphaltica.restaurantvoting.controller.VoteController.REST_URL;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = VoteController.REST_URL + '/';
    private static final String REST_URL_SLASH_VOTING_TODAY = REST_URL_SLASH + "profile-today";
    private static final String REST_URL_SLASH_VOTING_ALL = REST_URL_SLASH + "profile-all";

    @Autowired
    private VoteRepository repository;
    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(voteGuestRestaurant1));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getMyVotingToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH_VOTING_TODAY))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(voteGuestRestaurantNew));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getMyVotingAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH_VOTING_ALL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(voteGuestRestaurant1, voteGuestRestaurantNew));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void create() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL).param("restaurantId", String.valueOf(RESTAURANT1_ID)))
                .andDo(print());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        Vote newVote = getNewFromUser();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.findById(newId).get(), newVote);
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void update() throws Exception {
        dateTimeUtil.setEndOfVotingHours(LocalTime.now().getHour() + 1);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL).param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote updated = VOTE_MATCHER.readFromJson(action);
        int updatedId = updated.getId();
        VOTE_MATCHER.assertMatch(updated, voteGuestRestaurant2);
        VOTE_MATCHER.assertMatch(repository.findById(updatedId).get(), voteGuestRestaurant2);
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void updateFailForbiddenTime() throws Exception {
        dateTimeUtil.setEndOfVotingHours(LocalTime.now().getHour() - 1);
        perform(MockMvcRequestBuilders.post(REST_URL).param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
