package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.asphaltica.restaurantvoting.to.UserDto;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.mapper.UserMapper;
import ru.asphaltica.restaurantvoting.model.Role;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.service.UserService;
import ru.asphaltica.restaurantvoting.validation.UserValidator;

import java.net.URI;
import java.util.Set;

import static ru.asphaltica.restaurantvoting.util.ErrorsUtil.returnErrorsToClient;

@Tag(name = "Контроллер аккаунта", description = "Контроллер позволяет пользователю " +
        "совершать основные операции над аккаунтом, в том числе регистрироваться")
@RestController
@RequestMapping(value = "/api/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AccountController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AccountController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @Operation(
            summary = "Получение пользователя",
            description = "Позволяет пользователю получить данные о своем аккаунте"
    )
    @GetMapping
    public User get(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("get {}", userDetails.getUsername());
        return userService.findByMail(userDetails.getUsername());
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Позволяет пользователю удалить свой аккаунт"
    )
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("delete {}", userDetails.getUsername());
        userService.deleteByMail(userDetails.getUsername());
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет пользователю зарегистрировать свой аккаунт"
    )
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserDto userDTO, BindingResult bindingResult) {
        log.info("register {}", userDTO);
        User user = UserMapper.convertToUser(userDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityException(returnErrorsToClient(bindingResult));
        }
        user.setRoles(Set.of(Role.USER));
        User created = userService.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/account")
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Обновление пользователя",
            description = "Позволяет пользователю обновить данные своего аккаунта"
    )
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserDto userDTO, @AuthenticationPrincipal UserDetails userDetails) {
        User oldUser = userService.findByMail(userDetails.getUsername());
        User newUser = UserMapper.convertToUser(userDTO);
        log.info("update {} to {}", oldUser, newUser);
        newUser.setId(oldUser.getId());
        newUser.setRoles(oldUser.getRoles());
        userService.update(newUser);
    }
}
