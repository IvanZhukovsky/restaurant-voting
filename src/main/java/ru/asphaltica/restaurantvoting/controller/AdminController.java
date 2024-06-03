package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.asphaltica.restaurantvoting.model.User;

import java.net.URI;
import java.util.List;

@Tag(name = "Администрирование аккаунтов", description = "Позволяет администратору " +
        "совершать основные операции над аккаунтами пользователей")
@RestController
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminController extends AbstractUserController{

    public static final String REST_URL = "/api/admin/users";

    @Operation(
            summary = "Получение пользователей",
            description = "Позволяет администратору получить перечень всех зарегистрированных пользователей"
    )
    @GetMapping
    public List<User> getAll() {
        log.info("get all Users");
        return userService.findAll();
    }

    @Operation(
            summary = "Получение пользователя",
            description = "Позволяет администратору получить информацию о зарегистрированном пользователе по его id"
    )
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info("get user with id = {}", id);
        return userService.findById(id);
    }
    @Operation(
            summary = "Создание пользователя",
            description = "Позволяет администратору зарегистрировать пользователя на основе его данных"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("create new user");
        user.setId(null);
        User created = userService.create(user);
        log.info("A user has been created with id = {}", created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Позволяет администратору удалить аккаунт пользователя по его id"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        log.info("Removing a user with id = {}", id);
        userService.deleteById(id);
    }

    @Operation(
            summary = "Обновление пользователя",
            description = "Позволяет администратору обновить информацию о зарегистрированном пользователе c данным id"
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int id){
        log.info("Updating user with id = {}", id);
        user.setId(id);
        userService.update(user);
    }
    @Operation(
            summary = "Получение пользователя по email",
            description = "Позволяет администратору получить информацию о зарегистрированном пользователе по его email"
    )
    @Validated
    @GetMapping("/by-email")
    public User getByMail (@RequestParam @Email @NotBlank String email) {
        log.info("Get a user from email : {}", email);
        return userService.findByMail(email);
    }
}
