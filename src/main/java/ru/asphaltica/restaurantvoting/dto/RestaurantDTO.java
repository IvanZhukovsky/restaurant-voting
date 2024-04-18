package ru.asphaltica.restaurantvoting.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
public class RestaurantDTO {

    @NotEmpty
    private String name;
}
