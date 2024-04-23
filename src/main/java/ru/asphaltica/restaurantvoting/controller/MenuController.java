package ru.asphaltica.restaurantvoting.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.asphaltica.restaurantvoting.dto.DishDTO;
import ru.asphaltica.restaurantvoting.dto.MenuDTO;
import ru.asphaltica.restaurantvoting.dto.RestaurantDTO;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.service.*;
import ru.asphaltica.restaurantvoting.util.ErrorsUtil;
import ru.asphaltica.restaurantvoting.util.MenuValidator;
import ru.asphaltica.restaurantvoting.util.URIUtil;

import java.net.URI;
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
    private final MenuValidator menuValidator;

    @Autowired
    public MenuController(MenuService menuService, RestaurantService restaurantService, UserService userService, DishService dishService, VoteService voteService, ModelMapper modelMapper, MenuValidator menuValidator) {
        this.menuService = menuService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.dishService = dishService;
        this.voteService = voteService;
        this.modelMapper = modelMapper;
        this.menuValidator = menuValidator;
    }

    @GetMapping()
    public List<MenuDTO> getAll() {
        List<Menu> menus = menuService.findAll();
        return menus.stream().map(MenuDTO::convertToMenuDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MenuDTO get(@PathVariable int id) {
        return MenuDTO.convertToMenuDTO(menuService.findById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuDTO> findAllByOwnRestaurantId(@PathVariable int restaurantId) {
        List<Menu> menus = menuService.findAllByOwnRestaurantId(restaurantId);
        return menus.stream()
                .map(MenuDTO::convertToMenuDTO).collect(Collectors.toList());
    }

    @GetMapping("/today")
    public List<MenuDTO> findAllTodayAvailable() {
        return menuService.findAllTodayAvailable().stream().map(MenuDTO::convertToMenuDTO).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<MenuDTO> create(@Valid @RequestBody Menu menu, BindingResult bindingResult)
    {
        menuValidator.validate(menu, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityException(ErrorsUtil.returnErrorsToClient(bindingResult));
        }
        MenuDTO created = menuService.create(menu);
        return ResponseEntity.created(URIUtil.getCreatedUri("api/menus/{id}", created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        menuService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replace(@PathVariable int id, @Valid @RequestBody Menu menu, BindingResult bindingResult) {
        menu.setId(id);
        menuValidator.validate(menu, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityException(ErrorsUtil.returnErrorsToClient(bindingResult));
        }
        menuService.update(menu);
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

}
