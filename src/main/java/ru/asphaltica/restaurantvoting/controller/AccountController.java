package ru.asphaltica.restaurantvoting.controller;

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
import ru.asphaltica.restaurantvoting.dto.UserDTO;
import ru.asphaltica.restaurantvoting.exceptions.EntityException;
import ru.asphaltica.restaurantvoting.model.Role;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.service.UserService;
import ru.asphaltica.restaurantvoting.util.UserValidator;

import java.net.URI;
import java.util.Set;

import static ru.asphaltica.restaurantvoting.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping(value = "/api/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AccountController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountController(UserService userService, UserValidator userValidator, ModelMapper modelMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public User get(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("get {}", userDetails.getUsername());
        return userService.findByMail(userDetails.getUsername());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("delete {}", userDetails.getUsername());
        userService.deleteByMail(userDetails.getUsername());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        log.info("register {}", userDTO);
        User user = convertToUser(userDTO);
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
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserDTO userDTO, @AuthenticationPrincipal UserDetails userDetails) {
        User oldUser = userService.findByMail(userDetails.getUsername());
        User newUser = convertToUser(userDTO);
        log.info("update {} to {}", oldUser, newUser);
        newUser.setId(oldUser.getId());
        newUser.setRoles(oldUser.getRoles());
        if (newUser.getPassword() == null) {
            newUser.setPassword(oldUser.getPassword());
            userService.save(newUser);
        } else {
            userService.create(newUser);
        }

    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

}
