package ru.asphaltica.restaurantvoting;

import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.util.JsonUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static ru.asphaltica.restaurantvoting.RestaurantTestData.restaurant1;
import static ru.asphaltica.restaurantvoting.RestaurantTestData.restaurant2;

public class MenuTestData {

    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "");
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER_WITHOUT_RESTAURANT = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "ownRestaurant");
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER_WITHOUT_DISHES = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "dishes");

    public static final int MENU1_ID = 1;
    public static final int MENU2_ID = 2;
    public static final int MENU3_ID = 3;
    public static final int MENU4_ID = 4;
    public static final int NOT_FOUND_ID = 100;

    public static final Menu menu1 = new Menu(MENU1_ID, LocalDate.of(2024, 7, 10), restaurant1,  Set.of(
            new Dish("салат мимоза", new BigDecimal("30.30")),
            new Dish("суп гороховый", new BigDecimal("70.00")),
            new Dish("черный чай с сахаром", new BigDecimal("10.50")),
            new Dish("чай с травами", new BigDecimal("60.50"))
    ));

    public static final Menu menu2 = new Menu(MENU2_ID, LocalDate.of(2024, 7, 11), restaurant1,  Set.of(
            new Dish("чай с молоком", new BigDecimal("30.10")),
            new Dish("борщ", new BigDecimal("15.40")),
            new Dish("горчица", new BigDecimal("10.00")),
            new Dish("пирожное", new BigDecimal("7.58"))
    ));

    public static final Menu menu3 = new Menu(MENU3_ID, LocalDate.of(2024, 7, 10), restaurant2,  Set.of(
            new Dish("хинкали с бараниной", new BigDecimal("300.80")),
            new Dish("соус сацебели", new BigDecimal("50.50")),
            new Dish("хачапури", new BigDecimal("100.00")),
            new Dish("капучино", new BigDecimal("100.25")),
            new Dish("торт", new BigDecimal("78.55"))
    ));

    public static final Menu menu4 = new Menu(MENU4_ID, LocalDate.of(2024, 7, 11), restaurant2,  Set.of(
            new Dish("хинкали со свининой", new BigDecimal("250.50")),
            new Dish("соус томатный", new BigDecimal("46.40")),
            new Dish("латте", new BigDecimal("150.25")),
            new Dish("конфета", new BigDecimal("72.50"))
    ));

    public static Menu getUpdated() {
        return new Menu(MENU2_ID, LocalDate.of(2024, 7, 11), restaurant1,  Set.of(
                new Dish("чай с молоком", new BigDecimal("30.10")),
                new Dish("борщ", new BigDecimal("15.40")),
                new Dish("пирожное", new BigDecimal("7.58"))
        ));
    }

    public static final String JSON_WITH_DUPBLICATE_DISHES = "{\"id\":null,\"availableDate\":\"2024-07-11\",\"ownRestaurant\":{\"id\":1,\"name\":\"Щепка\"}," +
            "\"dishes\":[{\"name\":\"щи\",\"price\":13.20}, {\"name\":\"щи\",\"price\":13.20}, {\"name\":\"запеканка\",\"price\":9.99},{\"name\":\"чай с лимоном\",\"price\":30.60}]}";

    public static final String JSON_WITH_DUPBLICATE_NAMES_OF_DISHES = "{\"id\":null,\"availableDate\":\"2024-07-11\",\"ownRestaurant\":{\"id\":1,\"name\":\"Щепка\"}," +
            "\"dishes\":[{\"name\":\"щи\",\"price\":13.20}, {\"name\":\"щи\",\"price\":13.00}, {\"name\":\"запеканка\",\"price\":9.99},{\"name\":\"чай с лимоном\",\"price\":30.60}]}";


    public static Menu getNew() {
        return new Menu(null, LocalDate.of(2024, 7, 13), restaurant1,  Set.of(
                new Dish("чай с лимоном", new BigDecimal("30.60")),
                new Dish("щи", new BigDecimal("13.20")),
                new Dish("запеканка", new BigDecimal("9.99"))
        ));
    }

    public static Menu getWithDublicateDate() {
        return new Menu(null, LocalDate.of(2024, 7, 11), restaurant1,  Set.of(
                new Dish("чай с лимоном", new BigDecimal("30.60")),
                new Dish("запеканка", new BigDecimal("9.99"))
        ));
    }

    public static Menu getWithDupblicateDishes() {
        return new Menu(null, LocalDate.of(2024, 7, 12), restaurant1,  Set.of(
                new Dish("чай с лимоном", new BigDecimal("30.60")),
                new Dish("чай с лимоном", new BigDecimal("30.60")),
                new Dish("щи", new BigDecimal("13.20")),
                new Dish("запеканка", new BigDecimal("9.99"))
        ));
    }



}
