package ru.asphaltica.restaurantvoting.controller;

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

@Tag(name = "User methods")
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

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the restaurant voting";
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.findAll()
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
             throw new EntityException(returnErrorsToClient(bindingResult));
        }
        User created = userService.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        userService.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserDTO userDTO, @PathVariable int id, BindingResult bindingResult){
        User user = convertToUser(userDTO);
        if (bindingResult.hasErrors()) {
            throw new EntityException(returnErrorsToClient(bindingResult));
        }
        user.setId(id);
        userService.update(user);
    }

    @Validated
    @GetMapping("/by")
    public UserDTO getByMail (@RequestParam @Email @NotBlank String email) {
        return convertToUserDTO(userService.findByMail(email));
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
