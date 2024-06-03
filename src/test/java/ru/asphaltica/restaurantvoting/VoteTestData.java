package ru.asphaltica.restaurantvoting;

import ru.asphaltica.restaurantvoting.model.Vote;

import java.time.LocalDate;

import static ru.asphaltica.restaurantvoting.MenuTestData.*;
import static ru.asphaltica.restaurantvoting.UserTestData.guest;
import static ru.asphaltica.restaurantvoting.UserTestData.user;

public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user", "menu.dishes");

    public static final int VOTE1_ID = 1;
    public static final int VOTE2_ID = 2;
    public static final Vote voteGuestRestaurant1 = new Vote(VOTE1_ID, LocalDate.of(2024, 07, 11), guest, menu2);
    public static final Vote voteGuestRestaurantNew = new Vote(VOTE2_ID, LocalDate.now(), guest, menu5);
    public static final Vote voteGuestRestaurant2 = new Vote(VOTE2_ID, LocalDate.now(), guest, menu6);

    public static Vote getNewFromUser() {
        return new Vote(LocalDate.now(), user, menu5);
    }
}
