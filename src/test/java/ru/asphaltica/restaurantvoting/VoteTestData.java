package ru.asphaltica.restaurantvoting;

import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Vote;

import java.time.LocalDate;

import static ru.asphaltica.restaurantvoting.UserTestData.*;
import static ru.asphaltica.restaurantvoting.RestaurantTestData.*;



public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user.password");


    public static final int VOTE1_ID = 1;
    public static final int VOTE2_ID = 2;
    public static final Vote voteGuestRestaurant1 = new Vote(VOTE1_ID, LocalDate.now(), guest, restaurant1);
    public static final Vote voteGuestRestaurant2 = new Vote(VOTE1_ID, LocalDate.now(), guest, restaurant2);


    public static Vote getNewFromUser() {
        return new Vote(LocalDate.now(), user, restaurant1);
    }

}
