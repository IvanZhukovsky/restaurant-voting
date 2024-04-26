package ru.asphaltica.restaurantvoting.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

@Component
public class MenuValidator implements Validator {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Menu.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Menu menu = (Menu) target;
        if (!DateTimeUtil.isVotePeriod()) {
            errors.rejectValue("createDate", "New menus and update are not accepted today, you can submit a new menu tomorrow until 11:00");
        }
        if (menuRepository.existsByOwnRestaurantAndCreateDateBetween
                (menu.getOwnRestaurant(), DateTimeUtil.atStartOfToday(), DateTimeUtil.atEndOfVoting()) && menu.getId() == 0) {
                errors.rejectValue("createDate", "the menu has already been created for today");
        }
    }
}
