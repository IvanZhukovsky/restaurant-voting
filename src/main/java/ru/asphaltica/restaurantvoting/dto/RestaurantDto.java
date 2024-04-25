package ru.asphaltica.restaurantvoting.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.model.Restaurant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {

    private int id;
    @NotEmpty
    private String name;

    private static  ModelMapper modelMapper = new ModelMapper();

    public static Restaurant convertToRestaurant(RestaurantDto restaurantDTO) {
        return modelMapper.map(restaurantDTO, Restaurant.class);
    }

    public static RestaurantDto convertToRestaurantDTO(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDto.class);
    }


}
