package ru.asphaltica.restaurantvoting.mapper;

import org.springframework.stereotype.Component;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.to.MenuWithoutDishesDto;
import ru.asphaltica.restaurantvoting.to.MenuWithoutRestaurantDto;

@Component
public class MenuMapper {

    public MenuWithoutDishesDto converToWithoutDishesDto(Menu menu) {
        MenuWithoutDishesDto dto = new MenuWithoutDishesDto();
        dto.setId(menu.getId());
        dto.setAvailableDate(menu.getAvailableDate());
        dto.setOwnRestaurant(menu.getOwnRestaurant());
        return dto;
    }

    public MenuWithoutRestaurantDto convertToWithoutRestaurantDto(Menu menu) {
        MenuWithoutRestaurantDto dto = new MenuWithoutRestaurantDto();
        dto.setId(menu.getId());
        dto.setAvailableDate(menu.getAvailableDate());
        dto.setDishes(menu.getDishes());
        return dto;
    }
}
