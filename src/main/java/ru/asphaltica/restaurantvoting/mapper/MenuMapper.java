package ru.asphaltica.restaurantvoting.mapper;

import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.to.DishDto;
import ru.asphaltica.restaurantvoting.to.MenuDto;
import ru.asphaltica.restaurantvoting.to.RestaurantDto;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;

import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {

    private static ModelMapper modelMapper;

    private MenuMapper() {
    }

    public static MenuDto convertToMenuDTO(Menu menu) {
        ModelMapper modelMapper = new ModelMapper();
        MenuDto menuDTO = new MenuDto();
        menuDTO.setId(menu.getId());
        menuDTO.setCreateDate(menu.getCreateDate());
        RestaurantDto restaurantDTO = RestaurantMapper.convertToRestaurantDTO(menu.getOwnRestaurant());
        menuDTO.setOwnRestaurant(restaurantDTO);
        List<DishDto> dishes = menu.getDishes().stream().map(DishMapper::convertToDishDto).collect(Collectors.toList());
        menuDTO.setDishes(dishes);
        return menuDTO;
    }

    public static Menu converToMenu(MenuDto menuDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Menu menu = new Menu();
        menu.setId(menuDTO.getId());
        menu.setCreateDate(menuDTO.getCreateDate());
        Restaurant restaurant = RestaurantMapper.convertToRestaurant(menuDTO.getOwnRestaurant());
        menu.setOwnRestaurant(restaurant);
        List<Dish> dishes = null;
        if (menuDTO.getDishes() != null) {
            dishes = menuDTO.getDishes().stream().map(DishMapper::convertToDish).collect(Collectors.toList());
            menu.setDishes(dishes);}
        return menu;
    }
}
