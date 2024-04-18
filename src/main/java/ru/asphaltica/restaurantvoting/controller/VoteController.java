package ru.asphaltica.restaurantvoting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.service.VoteService;

import java.util.Collections;

@RestController
@RequestMapping("api/votes")
public class VoteController {

    private final VoteService voteService;

    private User principalUser = new User("iroads@mail.ru",
            "Ivan ",
            "Zhukovsky", "password",
            Collections.singleton(Role.ADMIN));

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }



}
