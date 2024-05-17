package ru.asphaltica.restaurantvoting.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Menu;

@Component
public class UniqueDishesNamesValidator implements Validator {

    public static final String EXCEPTION_DUPLICATE_DISHES_NAMES = "The menu contains the same names of dishes";

    @Override
    public boolean supports(Class<?> clazz) {
        return Menu.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Menu menu = (Menu) target;
        if (menu.getDishes() == null) return;
        int countUnicals = (int) menu.getDishes().stream().map(Dish::getName).distinct().count();
        int count = menu.getDishes().size();

        if (countUnicals != count) {
            errors.rejectValue("dishes", "", EXCEPTION_DUPLICATE_DISHES_NAMES);
        }
    }
}
