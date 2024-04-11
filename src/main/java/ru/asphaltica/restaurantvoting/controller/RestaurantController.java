package ru.asphaltica.restaurantvoting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the unprotected page";
    }

    @GetMapping("/all")
    //@PreAuthorize("hasAuthority('USER')")
    public List<Restaurant> allRestaurants() {
        return  restaurantService.findAll();
    }

    @GetMapping("/{id}")
    public Restaurant findById(@PathVariable int id) {
        return restaurantService.findById(id);
    }

    @PostMapping("/new")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public String add(@RequestBody Restaurant restaurant) {
        restaurantService.save(restaurant);
        return "Restaurant saved!";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable int id) {
        restaurantService.delete(id);
        return "Ресторан № " + id + " удален";
    }





}
