package ru.asphaltica.restaurantvoting.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.model.Restaurant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    private int id;
    @NotEmpty
    private String name;

    private static  ModelMapper modelMapper = new ModelMapper();

    public static Restaurant convertToRestaurant(RestaurantDTO restaurantDTO) {
        return modelMapper.map(restaurantDTO, Restaurant.class);
    }

    public static RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDTO.class);
    }


}
