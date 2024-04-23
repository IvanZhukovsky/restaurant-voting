package ru.asphaltica.restaurantvoting.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Restaurant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {

    private int id;

    @NotEmpty
    @Size(max = 128)
    private String name;

    @NotNull
    private Double price;

    private RestaurantDTO ownRestaurant;

    private static ModelMapper modelMapper = new ModelMapper();

    public static Dish convertToDish(DishDTO dishDTO) {
        return modelMapper.map(dishDTO, Dish.class);
    }

    public static DishDTO convertToDishDTO(Dish dish) {
        DishDTO dishDTO = modelMapper.map(dish, DishDTO.class);
        dishDTO.setOwnRestaurant(modelMapper.map(dish.getOwnRestaurant(), RestaurantDTO.class));
        return dishDTO;
    }
}
