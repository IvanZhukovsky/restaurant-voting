package ru.asphaltica.restaurantvoting.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.MenuDTO;
import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.service.DishService;
import ru.asphaltica.restaurantvoting.service.MenuService;
import ru.asphaltica.restaurantvoting.service.RestaurantService;
import ru.asphaltica.restaurantvoting.service.VoteService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;
    private final RestaurantService restaurantService;
    private final DishService dishService;
    private final VoteService voteService;
    private final ModelMapper modelMapper;

    private User principalUser = new User("iroads@mail.ru",
            "Ivan ",
            "Zhukovsky", "password",
            Collections.singleton(Role.ADMIN));

    @Autowired
    public MenuController(MenuService menuService, RestaurantService restaurantService, DishService dishService, VoteService voteService, ModelMapper modelMapper) {
        this.menuService = menuService;
        this.restaurantService = restaurantService;
        this.dishService = dishService;
        this.voteService = voteService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<MenuDTO> findAll() {
        return menuService.findAll().stream().map(this::convertToMenuDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MenuDTO findById(@PathVariable int id) {
        return convertToMenuDTO(menuService.findById(id));
    }

    @GetMapping("/today")
    public List<MenuDTO> findAllTodayAvailable() {
        return menuService.findAllTodayAvailable().stream().map(this::convertToMenuDTO).collect(Collectors.toList());
    }

    @PostMapping("/{restaurantId}")
    public String create(@PathVariable int restaurantId) {
        menuService.create(restaurantId);
        return "Create new menu!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        menuService.delete(id);
        return "Меню удалено";
    }

    @PutMapping("/{id}")
    public String replace(@PathVariable int id, @RequestBody MenuDTO menuDTO) {
        Menu menu = convertToMenu(menuDTO);
        menu.setId(id);
        menuService.update(menu);
        return "Меню обновлено!";
    }

    @PostMapping("/{menuId}/{dishId}")
    public String addDish(@PathVariable int menuId, @PathVariable int dishId) {
        Menu menu = menuService.findById(menuId);
        Dish dish = dishService.findById(dishId);
        menu.getDishes().add(dish);
        menuService.update(menu);
        return "Блюдо добавлено";
    }

    @PostMapping("/{menuId}/vote")
    public String createVote(@PathVariable int menuId) {
        Menu menu = new Menu();
        menu.setId(menuId);
        Vote vote = new Vote();
        vote.setMenu(menu);
        principalUser.setId(2);
        vote.setUser(principalUser);
        vote.setId(new UserMenuKey(2, menuId));
        voteService.create(vote);
        return principalUser.getFirstName() + " Вы проголосовали";
    }

    @GetMapping("/user")
    public String getPrincipalUser(@AuthenticationPrincipal UserDetails userDetails){
        return userDetails.getUsername();
    }


    private MenuDTO convertToMenuDTO(Menu menu) {
        return modelMapper.map(menu, MenuDTO.class);
    }

    private Menu convertToMenu(MenuDTO menuDTO) {
        return modelMapper.map(menuDTO, Menu.class);
    }

}
