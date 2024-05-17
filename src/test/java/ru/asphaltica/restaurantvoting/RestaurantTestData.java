package ru.asphaltica.restaurantvoting;

import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.model.Role;
import ru.asphaltica.restaurantvoting.model.User;

import java.util.Set;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "");
    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int NOT_FOUND_ID = 100;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Щепка");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Хинкальная");


    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT2_ID, "Хинкальная №1");
    }

    public static Restaurant getNew() {
        return new Restaurant( null, "Совершенно новый ресторан");
    }

    public static Restaurant getDublicate() {
        return new Restaurant( null, restaurant1.getName());
    }


}
