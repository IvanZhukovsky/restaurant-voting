package ru.asphaltica.restaurantvoting.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class MenuDTO {

    private LocalDateTime createDate;
    private Restaurant ownRestaurant;
    private List<Dish> dishes;
}
