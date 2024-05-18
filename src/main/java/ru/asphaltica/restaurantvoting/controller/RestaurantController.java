package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.mapper.RestaurantMapper;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.service.RestaurantService;
import ru.asphaltica.restaurantvoting.to.RestaurantDto;
import ru.asphaltica.restaurantvoting.util.URIUtil;
import ru.asphaltica.restaurantvoting.validation.RestaurantValidator;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Администрирование ресторанов и получения данных о них", description = "Позволяет администратору " +
        "совершать основные операции над списком ресторанов, а пользователям голосовать и получать данные о них")
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8")
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    public static final String REST_URL = "/api/restaurants";

    private final RestaurantService restaurantService;
    private final RestaurantValidator restaurantValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(restaurantValidator);
    }

    @Operation(
            summary = "Получение ресторанов",
            description = "Позволяет администратору получить перечень всех ресторанов"
    )
    @GetMapping
    public List<RestaurantDto> getAll() {
        log.info("get all restaurants");
        return restaurantService.findAll().stream()
                .map(RestaurantMapper::convertToRestaurantDTO)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Получение ресторана",
            description = "Позволяет администратору получить данные о ресторане по его id"
    )
    @GetMapping("/{id}")
    public RestaurantDto get(@PathVariable int id) {
        log.info("get restaurant with id = {}", id);
        return RestaurantMapper.convertToRestaurantDTO(restaurantService.findById(id));
    }

    @Operation(
            summary = "Создание нового ресторана",
            description = "Позволяет администратору создать новый ресторан"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8")
    public ResponseEntity<Restaurant> create(@RequestBody @Valid RestaurantDto restaurantDTO) {
        log.info("create new restaurant");
        Restaurant restaurant = RestaurantMapper.convertToRestaurant(restaurantDTO);
        Restaurant created = restaurantService.save(restaurant);
        log.info("A restaurant has been created with id = {}", created.getId());
        return ResponseEntity.created(URIUtil.getCreatedUri("/api/restaurants/{id}", created.getId())).body(created);
    }

    @Operation(
            summary = "Удаление ресторана",
            description = "Позволяет администратору удалить данные о ресторане по его id"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id) {
        log.info("Removing a restaurant with id = {}", id);
        restaurantService.delete(id);
    }

    @Operation(
            summary = "Обновление ресторана",
            description = "Позволяет администратору обновить данные о ресторане по его id"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody RestaurantDto restaurantDTO){
        log.info("Updating restaurant with id = {}", id);
        restaurantService.update(id, RestaurantMapper.convertToRestaurant(restaurantDTO));
    }

//    @Operation(
//            summary = "Голосование за ресторан",
//            description = "Позволяет пользователям голосовать за ресторан по его id"
//    )
//    @PostMapping("/{id}/vote")
//    public String createVote(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) {
//        log.info("Vote for restaurant with id = {}", id);
//        Menu menu = menuService.getAvailableMenu(id);
//        Vote vote = new Vote();
//        vote.setMenu(menu);
//
//        User authUser = userService.findByMail(userDetails.getUsername());
//        vote.setUser(authUser);
//        vote.setId(new UserMenuKey(authUser.getId(), menu.getId()));
//        return authUser.getFirstName()+ " " + voteService.create(vote);
//    }

//    @Operation(
//            summary = "Получение списка ресторанов доступных для голосования сегодня",
//            description = "Позволяет пользователям пользователям получать перечень ресторанов " +
//                    "доступных для голосования, в том числе меню дня"
//    )
//    @GetMapping("/available")
//    public List<Restaurant> findAllTodayAvailable() {
//        log.info("Getting a list of restaurants available for voting");
//        List<MenuDto> menuDtos = menuService.findAllTodayAvailable().stream().map(MenuMapper::convertToMenuDTO).collect(Collectors.toList());
//        return menuDtos.stream().map(menuDto -> {
//            Restaurant restaurant = RestaurantMapper.convertToRestaurant(menuDto.getOwnRestaurant());
//            restaurant.setMenus(List.of(MenuMapper.converToMenu(menuDto)));
//            return restaurant;
//        }).collect(toList());
//    }
}
