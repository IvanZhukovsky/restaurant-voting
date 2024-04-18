package ru.asphaltica.restaurantvoting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RestaurantsResponce {
    private List<RestaurantDTO> restaurants;
}
