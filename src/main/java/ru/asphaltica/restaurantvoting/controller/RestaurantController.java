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
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantService.findAll();
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
        restaurant.setId(null);
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

    @Operation(
            summary = "Список ресторанов у которых сегодня есть меню",
            description = "Позволяет зарегистрированным пользователям получить список " +
                    "ресторанов в которых сегодня есть меню"
    )
    @GetMapping("/available")
    public List<Restaurant> findAllTodayAvailable() {
        log.info("get restaurants available to vote today");
        return restaurantService.findAllTodayAvailable();
    }
}
