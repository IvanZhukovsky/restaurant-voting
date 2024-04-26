package ru.asphaltica.restaurantvoting.mapper;

import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.to.RestaurantDto;
import ru.asphaltica.restaurantvoting.model.Restaurant;

public class RestaurantMapper {

    private static ModelMapper modelMapper = new ModelMapper();

    private RestaurantMapper() {
    }

    public static Restaurant convertToRestaurant(RestaurantDto restaurantDTO) {
        return modelMapper.map(restaurantDTO, Restaurant.class);
    }

    public static RestaurantDto convertToRestaurantDTO(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDto.class);
    }
}
