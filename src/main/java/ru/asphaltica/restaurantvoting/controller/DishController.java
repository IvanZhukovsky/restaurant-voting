package ru.asphaltica.restaurantvoting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.MenuDTO;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public List<Dish> getAll() {
        return dishService.findAll();
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        return dishService.findById(id);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<Dish> findAllByOwnRestaurantId(@PathVariable int restaurantId){
        return dishService.findAllByOwnRestaurantId(restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestBody Dish dish) {
        dishService.create(dish);
        return "Новое блюдо сохранено";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        dishService.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int id){
        dish.setId(id);
        dishService.update(dish);
    }

}
