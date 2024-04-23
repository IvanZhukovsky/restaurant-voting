package ru.asphaltica.restaurantvoting.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.DishDTO;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.service.DishService;
import ru.asphaltica.restaurantvoting.util.DishValidator;
import ru.asphaltica.restaurantvoting.util.URIUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ru.asphaltica.restaurantvoting.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping(value = "api/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    private final DishService dishService;
    private final ModelMapper modelMapper;
    private final DishValidator dishValidator;


    @Autowired
    public DishController(DishService dishService, ModelMapper modelMapper, DishValidator dishValidator) {
        this.dishService = dishService;
        this.modelMapper = modelMapper;
        this.dishValidator = dishValidator;
    }

    @GetMapping
    public List<DishDTO> getAll() {
        return dishService.findAll().stream().map(DishDTO::convertToDishDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DishDTO get(@PathVariable int id) {
        return DishDTO.convertToDishDTO(dishService.findById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<Dish> findAllByOwnRestaurantId(@PathVariable int restaurantId){
        return dishService.findAllByOwnRestaurantId(restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@RequestBody @Valid DishDTO dishDTO, @RequestParam int rest_id, BindingResult bindingResult) {
        Dish dish = convertToDish(dishDTO);
        Restaurant ownRestaurant = new Restaurant();
        ownRestaurant.setId(rest_id);
        dish.setOwnRestaurant(ownRestaurant);
        dishValidator.validate(dish, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityException(returnErrorsToClient(bindingResult));
        }
        Dish created = dishService.create(dish);
        return ResponseEntity.created(URIUtil.getCreatedUri("/api/dishes/{id}", created.getId())).body(created);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        dishService.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody DishDTO dishDTO, @PathVariable int id){
        Dish dish = convertToDish(dishDTO);
        dish.setId(id);
        dishService.update(dish);
    }

    private Dish convertToDish(DishDTO dishDTO) {
        return modelMapper.map(dishDTO, Dish.class);
    }

    private DishDTO convertToDishDTO(Dish dish) {
        return modelMapper.map(dish, DishDTO.class);
    }

}
