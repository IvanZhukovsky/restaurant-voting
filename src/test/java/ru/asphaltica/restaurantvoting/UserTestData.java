package ru.asphaltica.restaurantvoting;

import ru.asphaltica.restaurantvoting.model.Role;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.util.JsonUtil;

import java.util.Collections;
import java.util.Set;

public class UserTestData {

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "password");

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int GUEST_ID = 3;
    public static final int NOT_FOUND = 100;
    public static final String ADMIN_MAIL = "admin@javaops.ru";
    public static final String USER_MAIL = "user@gmail.com";
    public static final String GUEST_MAIL = "guest@mail.ru";

    public static final User user = new User(USER_ID, USER_MAIL, "User_First", "User_Last",
            "$2a$10$TG8ZznZ1srJZa6Vegvq/XuKdcdrslMHALOxDzY20wbpWOvl.YReoy",Collections.singleton(Role.USER));
    public static final User admin = new User(ADMIN_ID, ADMIN_MAIL, "Admin_First", "Admin_Last",
            "$2a$10$MTMrh7X8UslRu2Dq5srro.d7oBjJQDz7b66xjViVyxsRwh4y0YCb.", Set.of(Role.USER, Role.ADMIN));
    public static final User guest = new User(GUEST_ID, GUEST_MAIL, "Guest_First", "Guest_Last",
            "$2a$10$MTMrh7X8UslRu2Dq5srro.d7oBjJQDz7b66xjViVyxsRwh4y0YCb.", Collections.singleton(Role.USER));

    public static User getNew() {
        return new User(null, "new@gmail.com","NewFirstName", "NewLastName", "newPass",  Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, USER_MAIL,"UpdatedFirstName",  "UpdatedLastName","newPass", Set.of(Role.USER));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

    public static void main(String[] args) {
        System.out.println(USER_MATCHER.contentJson(user));
    }
}
