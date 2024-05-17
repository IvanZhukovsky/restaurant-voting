package ru.asphaltica.restaurantvoting.to;

import lombok.Getter;
import lombok.Setter;
import ru.asphaltica.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class MenuWithoutRestaurantDto {
    int id;
    LocalDate availableDate;
    Set<Dish> dishes;
}
