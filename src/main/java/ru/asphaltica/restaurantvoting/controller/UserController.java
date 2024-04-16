package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.service.UserService;

import java.util.List;

@Tag(name = "User methods")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        userService.addUser(user);
        return userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id){
        userService.deleteById(id);
        return "User id = " + id + " удален!";
    }
}
