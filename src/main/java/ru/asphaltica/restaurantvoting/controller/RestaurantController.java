package ru.asphaltica.restaurantvoting.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.MenuDTO;
import ru.asphaltica.restaurantvoting.dto.RestaurantDTO;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.service.MenuService;
import ru.asphaltica.restaurantvoting.service.RestaurantService;
import ru.asphaltica.restaurantvoting.service.UserService;
import ru.asphaltica.restaurantvoting.service.VoteService;
import ru.asphaltica.restaurantvoting.util.RestaurantValidator;
import ru.asphaltica.restaurantvoting.util.URIUtil;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static ru.asphaltica.restaurantvoting.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping(value = "/api/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final MenuService menuService;
    private final ModelMapper modelMapper;
    private final VoteService voteService;
    private final RestaurantValidator restaurantValidator;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, UserService userService, MenuService menuService, ModelMapper modelMapper, VoteService voteService, RestaurantValidator restaurantValidator) {
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.menuService = menuService;
        this.modelMapper = modelMapper;
        this.voteService = voteService;
        this.restaurantValidator = restaurantValidator;
    }


    @GetMapping
    //@PreAuthorize("hasAuthority('USER')")
    public List<RestaurantDTO> getAll() {
        return restaurantService.findAll().stream()
                .map(this::convertToRestaurantDTO)
                .collect(toList());
    }

    @GetMapping("/{id}")
    public RestaurantDTO get(@PathVariable int id) {
        return convertToRestaurantDTO(restaurantService.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Restaurant> create(@RequestBody @Valid RestaurantDTO restaurantDTO, BindingResult bindingResult) {
        Restaurant restaurant = convertToRestaurant(restaurantDTO);
        restaurantValidator.validate(restaurant, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityException(returnErrorsToClient(bindingResult));
        }
        Restaurant created = restaurantService.save(restaurant);
        return ResponseEntity.created(URIUtil.getCreatedUri("/api/restaurants/{id}", created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id) {
        restaurantService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody RestaurantDTO restaurantDTO){
        restaurantService.update(id, convertToRestaurant(restaurantDTO));
    }

    @PostMapping("/{id}/vote")
    public String createVote(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) {
        Menu menu = menuService.getAvailableMenu(id);
        Vote vote = new Vote();
        vote.setMenu(menu);

        User authUser = userService.findByMail(userDetails.getUsername());
        vote.setUser(authUser);
        vote.setId(new UserMenuKey(authUser.getId(), menu.getId()));
        return authUser.getFirstName() + voteService.create(vote);
    }

    @GetMapping("/available")
    public List<Restaurant> findAllTodayAvailable() {
        List<MenuDTO> menuDTOS = menuService.findAllTodayAvailable().stream().map(MenuDTO::convertToMenuDTO).collect(Collectors.toList());

        return menuDTOS.stream().map(menuDTO -> {
            Restaurant restaurant = convertToRestaurant(menuDTO.getOwnRestaurant());
            restaurant.setMenus(List.of(MenuDTO.converToMenu(menuDTO)));
            return restaurant;
        }).collect(toList());
    }

    private Restaurant convertToRestaurant(RestaurantDTO restaurantDTO) {
        return modelMapper.map(restaurantDTO, Restaurant.class);
    }

    private RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDTO.class);
    }




}
