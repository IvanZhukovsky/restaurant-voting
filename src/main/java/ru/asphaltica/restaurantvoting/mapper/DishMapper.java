package ru.asphaltica.restaurantvoting.mapper;

import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.to.DishDto;
import ru.asphaltica.restaurantvoting.model.Dish;

public class DishMapper {
    private DishMapper() {
    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static Dish convertToDish(DishDto dishDto) {
        return modelMapper.map(dishDto, Dish.class);
    }

    public static DishDto convertToDishDto(Dish dish) {
        return modelMapper.map(dish, DishDto.class);
    }

}
