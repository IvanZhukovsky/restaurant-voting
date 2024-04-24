package ru.asphaltica.restaurantvoting.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.repository.RestaurantRepository;

@Component
public class RestaurantValidator implements Validator {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantValidator(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Restaurant.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Restaurant restaurant = (Restaurant) target;
        if (restaurantRepository.existsByNameIgnoreCase(restaurant.getName())) {
            errors.rejectValue("name", "a restaurant with this name is already created");
        }
    }
}
