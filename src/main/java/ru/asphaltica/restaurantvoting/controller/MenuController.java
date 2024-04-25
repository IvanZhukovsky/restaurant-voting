package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.MenuDto;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.mapper.MenuMapper;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.service.*;
import ru.asphaltica.restaurantvoting.util.ErrorsUtil;
import ru.asphaltica.restaurantvoting.util.MenuValidator;
import ru.asphaltica.restaurantvoting.util.URIUtil;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Контроллер управления меню", description = "Контроллер позволяет администратору " +
        "совершать основные операции с меню")
@RestController
@RequestMapping("/api/menus")
@Slf4j
public class MenuController {

    private final MenuService menuService;
    private final MenuValidator menuValidator;

    @Autowired
    public MenuController(MenuService menuService, MenuValidator menuValidator) {
        this.menuService = menuService;
        this.menuValidator = menuValidator;
    }

    @Operation(
            summary = "Получение полного перечня созданных меню",
            description = "Позволяет администратору получить полный перечень всех, когда либо созданных меню"
    )
    @GetMapping()
    public List<MenuDto> getAll() {
        log.info("get all menus");
        List<Menu> menus = menuService.findAll();
        return menus.stream().map(MenuMapper::convertToMenuDTO).collect(Collectors.toList());
    }
    @Operation(
            summary = "Получение меню",
            description = "Позволяет администратору получить меню по его id"
    )
    @GetMapping("/{id}")
    public MenuDto get(@PathVariable int id) {
        return MenuMapper.convertToMenuDTO(menuService.findById(id));
    }

    @Operation(
            summary = "Получение списка меню в конкретном ресторане",
            description = "Позволяет администратору получить перечень всех меню, созданных для ресторана с указанным id"
    )
    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuDto> findAllByOwnRestaurantId(@PathVariable int restaurantId) {
        log.info("get all menus in restaurant with id = {}", restaurantId);
        List<Menu> menus = menuService.findAllByOwnRestaurantId(restaurantId);
        return menus.stream()
                .map(MenuMapper::convertToMenuDTO).collect(Collectors.toList());
    }

    @Operation(
            summary = "Получение списка меню созданных в текущий день",
            description = "Позволяет администратору получить перечень всех меню созданных в текущий день"
    )
    @GetMapping("/today")
    public List<MenuDto> findAllTodayAvailable() {
        log.info("get menus available today");
        return menuService.findAllTodayAvailable().stream().map(MenuMapper::convertToMenuDTO).collect(Collectors.toList());
    }

    @Operation(
            summary = "Создание меню",
            description = "Позволяет администратору создать новое меню"
    )
    @PostMapping
    public ResponseEntity<MenuDto> create(@Valid @RequestBody Menu menu, BindingResult bindingResult)
    {
        log.info("create new menu");
        menuValidator.validate(menu, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityException(ErrorsUtil.returnErrorsToClient(bindingResult));
        }
        log.info("Validation of new menu data passed");
        MenuDto created = menuService.create(menu);
        log.info("A menu has been created with id = {}", created.getId());
        return ResponseEntity.created(URIUtil.getCreatedUri("api/menus/{id}", created.getId())).body(created);
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
            summary = "Обновление меню",
            description = "Позволяет администратору обновить меню по его id"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replace(@PathVariable int id, @Valid @RequestBody Menu menu, BindingResult bindingResult) {
        log.info("Updating menu with id = {}", id);
        menu.setId(id);
        menuValidator.validate(menu, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityException(ErrorsUtil.returnErrorsToClient(bindingResult));
        }
        menuService.update(menu);
    }
}
