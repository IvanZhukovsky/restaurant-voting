package ru.asphaltica.restaurantvoting.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.repository.DishRepository;

@Component
public class DishValidator implements Validator {

    private final DishRepository dishRepository;

    @Autowired
    public DishValidator(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Dish.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Dish dish = (Dish) target;
        if (dishRepository.findByName(dish.getName()).isPresent()) {
            errors.rejectValue("name", "a dish with this name is already created");
        }
    }
}
