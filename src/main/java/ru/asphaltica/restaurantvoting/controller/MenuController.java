package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.mapper.MenuMapper;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.service.MenuService;
import ru.asphaltica.restaurantvoting.to.MenuWithoutDishesDto;
import ru.asphaltica.restaurantvoting.to.MenuWithoutRestaurantDto;
import ru.asphaltica.restaurantvoting.util.URIUtil;
import ru.asphaltica.restaurantvoting.validation.UniqueDishesNamesValidator;

import java.util.List;

@Tag(name = "Управление меню", description = "Позволяет администратору " +
        "совершать основные операции с меню")
@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@Slf4j
@AllArgsConstructor
public class MenuController {

    public static final String REST_URL = "/api/menus";
    private final MenuService menuService;
    private final UniqueDishesNamesValidator uniqueDishesNamesValidator;
    private final MenuMapper menuMapper;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(uniqueDishesNamesValidator);
    }

    @Operation(
            summary = "Получение полного перечня созданных меню, без списка блюд",
            description = "Позволяет администратору получить полный перечень всех, когда либо созданных меню"
    )
    @GetMapping()
    public List<MenuWithoutDishesDto> getAll() {
        log.info("get all menus");
        return menuService.findAll().stream().map(menuMapper::converToWithoutDishesDto).toList();
    }

    @Operation(
            summary = "Получение меню",
            description = "Позволяет администратору получить меню по его id"
    )
    @GetMapping("/{id}")
    public Menu get(@PathVariable int id) {
        return menuService.findByIdWithRestaurantAndDishes(id);
    }

    @Operation(
            summary = "Удаление меню",
            description = "Позволяет администратору удалить меню по его id"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Removing a menu with id = {}", id);
        menuService.delete(id);
    }

    @Operation(
            summary = "Создание меню",
            description = "Позволяет администратору создать новое меню"
    )
    @PostMapping
    public ResponseEntity<Menu> create(@Valid @RequestBody Menu menu) {
        log.info("create new menu");
        Menu created = menuService.create(menu);
        log.info("A menu has been created with id = {}", created.getId());
        return ResponseEntity.created(URIUtil.getCreatedUri("api/menus/{id}", created.getId())).body(created);
    }

    @Operation(
            summary = "Обновление меню",
            description = "Позволяет администратору обновить меню по его id"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Menu menu) {
        log.info("Updating menu with id = {}", id);
        menu.setId(id);
        menuService.update(menu);
    }

    @Operation(
            summary = "Получение списка меню в конкретном ресторане",
            description = "Позволяет администратору получить перечень всех меню, созданных для ресторана с указанным id"
    )
    @Validated
    @GetMapping("/by-restaurant")
    public List<MenuWithoutRestaurantDto> findAllByOwnRestaurantId(@RequestParam @NotNull int restaurantId) {
        log.info("get all menus in restaurant with id = {}", restaurantId);
        return menuService.findAllByOwnRestaurantId(restaurantId).stream().map(menuMapper::convertToWithoutRestaurantDto).toList();
    }
}
