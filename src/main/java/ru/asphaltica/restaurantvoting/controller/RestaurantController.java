package ru.asphaltica.restaurantvoting.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.asphaltica.restaurantvoting.dto.MenuDTO;
import ru.asphaltica.restaurantvoting.dto.RestaurantDTO;
import ru.asphaltica.restaurantvoting.dto.RestaurantsResponce;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.service.MenuService;
import ru.asphaltica.restaurantvoting.service.RestaurantService;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.asphaltica.restaurantvoting.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping(value = "/api/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    //@PreAuthorize("hasAuthority('USER')")
    public RestaurantsResponce getAll() {
        return new RestaurantsResponce(restaurantService.findAll().stream()
                .map(this::convertToRestaurantDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public RestaurantDTO get(@PathVariable int id) {
        return convertToRestaurantDTO(restaurantService.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Restaurant> create(@RequestBody @Valid RestaurantDTO restaurantDTO, BindingResult bindingResult) {
        Restaurant restaurant = convertToRestaurant(restaurantDTO);
        if (bindingResult.hasErrors()) {
            throw new EntityException(returnErrorsToClient(bindingResult));
        }
        Restaurant created = restaurantService.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable int id) {
        restaurantService.delete(id);
        return "Ресторан № " + id + " удален";
    }

    @PutMapping("/{id}")

    public String update(@PathVariable int id, @RequestBody RestaurantDTO restaurantDTO){
        if (!restaurantService.existById(id)) {
            return "No such row";
        }
        restaurantService.save(convertToRestaurant(restaurantDTO));
        return "Restaurant updated";
    }

    private Restaurant convertToRestaurant(RestaurantDTO restaurantDTO) {
        return modelMapper.map(restaurantDTO, Restaurant.class);
    }

    private RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDTO.class);
    }
}
