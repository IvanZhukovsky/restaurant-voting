package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.asphaltica.restaurantvoting.dto.UserDTO;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.service.UserService;
import ru.asphaltica.restaurantvoting.util.UserValidator;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ru.asphaltica.restaurantvoting.util.ErrorsUtil.returnErrorsToClient;

@Tag(name = "Контроллер администрирования аккаунтов", description = "Контроллер позволяет администратору " +
        "совершать основные операции над аккаунтами пользователей")
@RestController
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminController {

    public static final String REST_URL = "/api/admin/users";
    private final UserService userService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, ModelMapper modelMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
    }

    @Operation(
            summary = "Получение пользователей",
            description = "Позволяет администратору получить перечень всех зарегистрированных пользователей"
    )
    @GetMapping
    public List<UserDTO> getAll() {
        log.info("get all Users");
        return userService.findAll()
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
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
    public ResponseEntity<User> create(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        log.info("create new user");
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
             throw new EntityException(returnErrorsToClient(bindingResult));
        }
        log.info("Validation of new user data passed");
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
            description = "Позволяет администратору обновить информацию о зарегистрированном пользователе по его id"
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserDTO userDTO, @PathVariable int id, BindingResult bindingResult){
        log.info("Updating user with id = {}", id);
        User user = convertToUser(userDTO);
        if (bindingResult.hasErrors()) {
            throw new EntityException(returnErrorsToClient(bindingResult));
        }
        user.setId(id);
        userService.update(user);
    }
    @Operation(
            summary = "Получение пользователя по email",
            description = "Позволяет администратору получить информацию о зарегистрированном пользователе по его email"
    )
    @Validated
    @GetMapping("/by")
    public UserDTO getByMail (@RequestParam @Email @NotBlank String email) {
        log.info("Get a user from email : {}", email);
        return convertToUserDTO(userService.findByMail(email));
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
