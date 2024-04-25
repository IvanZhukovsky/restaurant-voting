package ru.asphaltica.restaurantvoting.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.model.Dish;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {

    private int id;

    @NotEmpty
    @Size(max = 128)
    private String name;

    @NotNull
    private Double price;

    private RestaurantDto ownRestaurant;

    private static ModelMapper modelMapper = new ModelMapper();

    public static Dish convertToDish(DishDto dishDTO) {
        return modelMapper.map(dishDTO, Dish.class);
    }

    public static DishDto convertToDishDTO(Dish dish) {
        DishDto dishDTO = modelMapper.map(dish, DishDto.class);
        dishDTO.setOwnRestaurant(modelMapper.map(dish.getOwnRestaurant(), RestaurantDto.class));
        return dishDTO;
    }
}
