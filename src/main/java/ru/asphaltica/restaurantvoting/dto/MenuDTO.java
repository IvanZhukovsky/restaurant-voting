package ru.asphaltica.restaurantvoting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MenuDTO {

    private int id;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd : HH-mm-ss")
    private LocalDateTime createDate;
    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private RestaurantDTO ownRestaurant;
    private List<DishDTO> dishes;

    public static MenuDTO convertToMenuDTO(Menu menu) {
        ModelMapper modelMapper = new ModelMapper();
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(menu.getId());
        menuDTO.setCreateDate(menu.getCreateDate());
        RestaurantDTO restaurantDTO = modelMapper.map(menu.getOwnRestaurant(), RestaurantDTO.class);
        menuDTO.setOwnRestaurant(restaurantDTO);
        List<DishDTO> dishes = menu.getDishes().stream().map(dish -> modelMapper.map(dish, DishDTO.class)).collect(Collectors.toList());
        menuDTO.setDishes(dishes);
        return menuDTO;
    }

    public static Menu converToMenu(MenuDTO menuDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Menu menu = new Menu();
        menu.setId(menuDTO.getId());
        menu.setCreateDate(menuDTO.getCreateDate());
        Restaurant restaurant = modelMapper.map(menuDTO.getOwnRestaurant(), Restaurant.class);
        menu.setOwnRestaurant(restaurant);
        List<Dish> dishes = null;
        if (menuDTO.getDishes() != null) {
        dishes = menuDTO.getDishes().stream().map(dishDTO -> modelMapper.map(dishDTO, Dish.class)).collect(Collectors.toList());
        menu.setDishes(dishes);}
        return menu;
    }
}
