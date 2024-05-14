package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.to.DishDto;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.mapper.DishMapper;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.service.DishService;
import ru.asphaltica.restaurantvoting.validation.DishValidator;
import ru.asphaltica.restaurantvoting.util.URIUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ru.asphaltica.restaurantvoting.util.ErrorsUtil.returnErrorsToClient;

@Tag(name = "Контроллер администрирования списка блюд в ресторанах", description = "Контроллер позволяет администратору " +
        "совершать основные операции над списком блюд")
@RestController
@RequestMapping(value = "api/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {

    private final DishService dishService;
    private final DishValidator dishValidator;

    @Operation(
            summary = "Получение блюд",
            description = "Позволяет администратору получить перечень всех блюд с указанием id ресторана, " +
                    "которому оно принадлежит"
    )
    @GetMapping
    public List<DishDto> getAll() {
        log.info("get all dishes");
        return dishService.findAll().stream().map(DishDto::convertToDishDTO).collect(Collectors.toList());
    }

    @Operation(
            summary = "Получение блюда",
            description = "Позволяет администратору получить информацию о блюде по его id"
    )
    @GetMapping("/{id}")
    public DishDto get(@PathVariable int id) {
        log.info("get dish with id = {}", id);
        return DishDto.convertToDishDTO(dishService.findById(id));
    }

    @Operation(
            summary = "Получение блюд",
            description = "Позволяет администратору получить перечень всех блюд доступных в ресторане по его id, "
    )
    @GetMapping("/restaurant/{restaurantId}")
    public List<Dish> findAllByOwnRestaurantId(@PathVariable int restaurantId){
        log.info("get all dishes in reastaurant with id = {}", restaurantId);
        return dishService.findAllByOwnRestaurantId(restaurantId);
    }

    @Operation(
            summary = "Создание блюда",
            description = "Позволяет администратору создать блюдо и зарегистрировать его в ресторане с передаваемым " +
                    "в качестве параметра id, "
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@RequestBody @Valid DishDto dishDTO,
                                       @RequestParam int restaurantId, BindingResult bindingResult) {
        log.info("create new dish");
        Dish dish = DishMapper.convertToDish(dishDTO);
        Restaurant ownRestaurant = new Restaurant();
        ownRestaurant.setId(restaurantId);
        dish.setOwnRestaurant(ownRestaurant);
        dishValidator.validate(dish, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityException(returnErrorsToClient(bindingResult));
        }
        log.info("Validation of new dish data passed");
        Dish created = dishService.create(dish);
        log.info("A dish has been created with id = {}", created.getId());
        return ResponseEntity.created(URIUtil.getCreatedUri("/api/dishes/{id}", created.getId())).body(created);
    }

    @Operation(
            summary = "Удаление блюда",
            description = "Позволяет администратору удалить блюдо по его id"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        log.info("Removing a dish with id = {}", id);
        dishService.deleteById(id);
    }

    @Operation(
            summary = "Обновление блюда",
            description = "Позволяет администратору обновить блюдо по его id"
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody DishDto dishDTO, @PathVariable int id){
        log.info("Updating dish with id = {}", id);
        Dish dish = DishMapper.convertToDish(dishDTO);
        dish.setId(id);
        dishService.update(dish);
    }


}
