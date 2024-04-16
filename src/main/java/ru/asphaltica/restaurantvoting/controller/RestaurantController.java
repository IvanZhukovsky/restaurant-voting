package ru.asphaltica.restaurantvoting.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.MenuDTO;
import ru.asphaltica.restaurantvoting.dto.RestaurantDTO;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.service.MenuService;
import ru.asphaltica.restaurantvoting.service.RestaurantService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final ModelMapper modelMapper;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, MenuService menuService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the unprotected page";
    }

    //Получить все рестораны
    @GetMapping
    //@PreAuthorize("hasAuthority('USER')")
    public List<RestaurantDTO> allRestaurants() {
        return restaurantService.findAll().stream()
                .map(this::convertToRestaurantDTO)
                .collect(Collectors.toList());
    }

    //Получить ресторан по его id
    @GetMapping("/{id}")
    public RestaurantDTO findById(@PathVariable int id) {
        return convertToRestaurantDTO(restaurantService.findById(id));
    }

    //Получить все меню ресторана по его id
    @GetMapping("/{id}/menus")
    public List<MenuDTO> findAllByOwnRestaurantId(@PathVariable int id){
        List<Menu> menus = menuService.findAllByOwnRestaurantId(id);
        List<MenuDTO> menusDTO = menus.stream()
                .map(this::convertToMenuDTO).collect(Collectors.toList());
         return menusDTO;
    }

    //Добавить новый ресторан
    @PostMapping
    //@PreAuthorize("hasAuthority('ADMIN')")
    public String add(@RequestBody @Valid RestaurantDTO restaurantDTO) {
        restaurantService.save(convertToRestaurant(restaurantDTO));
        return "Restaurant saved!";
    }

    //Изменить ресторан
    @PatchMapping("/{id}")
    public String updateById(@PathVariable int id, @RequestBody RestaurantDTO restaurantDTO){
        Restaurant restaurant = restaurantService.findById(id);
        Restaurant patch = convertToRestaurant(restaurantDTO);
        if (patch.getName() != null) {
            restaurant.setName(patch.getName());
        }
        restaurantService.save(restaurant);
        return "restaurant updated";
    }


    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable int id) {
        restaurantService.delete(id);
        return "Ресторан № " + id + " удален";
    }

    private Restaurant convertToRestaurant(RestaurantDTO restaurantDTO) {
        return modelMapper.map(restaurantDTO, Restaurant.class);
    }

    private RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDTO.class);
    }

    private MenuDTO convertToMenuDTO(Menu menu) {
        return modelMapper.map(menu, MenuDTO.class);
    }


}
