package ru.asphaltica.restaurantvoting.to;

import lombok.Getter;
import lombok.Setter;
import ru.asphaltica.restaurantvoting.model.Restaurant;

import java.time.LocalDate;

@Getter
@Setter
public class MenuWithoutDishesDto {

    int id;
    LocalDate availableDate;
    Restaurant ownRestaurant;

}
