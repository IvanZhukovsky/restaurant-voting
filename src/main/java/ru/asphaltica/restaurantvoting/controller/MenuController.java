package ru.asphaltica.restaurantvoting.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.MenuDTO;
import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.service.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final DishService dishService;
    private final VoteService voteService;
    private final ModelMapper modelMapper;

    private User principalUser = new User("iroads@mail.ru",
            "Ivan ",
            "Zhukovsky", "password",
            Collections.singleton(Role.ADMIN));

    @Autowired
    public MenuController(MenuService menuService, RestaurantService restaurantService, UserService userService, DishService dishService, VoteService voteService, ModelMapper modelMapper) {
        this.menuService = menuService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.dishService = dishService;
        this.voteService = voteService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<MenuDTO> getAll() {
        return menuService.findAll().stream().map(this::convertToMenuDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MenuDTO get(@PathVariable int id) {
        return convertToMenuDTO(menuService.findById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuDTO> findAllByOwnRestaurantId(@PathVariable int restaurantId){
        List<Menu> menus = menuService.findAllByOwnRestaurantId(restaurantId);
        return menus.stream()
                .map(this::convertToMenuDTO).collect(Collectors.toList());
    }

    @GetMapping("/today")
    public List<MenuDTO> findAllTodayAvailable() {
        return menuService.findAllTodayAvailable().stream().map(this::convertToMenuDTO).collect(Collectors.toList());
    }

    @PostMapping("/{restaurantId}")
    public String create(@PathVariable int restaurantId, @RequestBody MenuDTO menuDTO) {
        menuService.create(restaurantId, convertToMenu(menuDTO));
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
    public String createVote(@PathVariable int menuId, @AuthenticationPrincipal UserDetails userDetails) {
        Menu menu = new Menu();
        menu.setId(menuId);

        Vote vote = new Vote();
        vote.setMenu(menu);

        User authUser = userService.findByMail(userDetails.getUsername());
        vote.setUser(authUser);
        vote.setId(new UserMenuKey(authUser.getId(), menuId));
        return authUser.getFirstName() + voteService.create(vote);
    }

    private MenuDTO convertToMenuDTO(Menu menu) {
        return modelMapper.map(menu, MenuDTO.class);
    }

    private Menu convertToMenu(MenuDTO menuDTO) {
        return modelMapper.map(menuDTO, Menu.class);
    }

}
