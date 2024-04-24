package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.RestaurantDTO;
import ru.asphaltica.restaurantvoting.dto.VotingResult;
import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.service.MenuService;
import ru.asphaltica.restaurantvoting.service.VoteService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Контроллер получения данных о голосовании",
        description = "Контроллер позволяет пользователям получать текущие результаты голосования за рестораны" )
@RestController
@RequestMapping("api/votes")
@Slf4j
public class VoteController {

    private final VoteService voteService;
    private final MenuService menuService;

    @Autowired
    public VoteController(VoteService voteService, MenuService menuService) {
        this.voteService = voteService;
        this.menuService = menuService;
    }


    @Operation(
            summary = "Получение результатов голосования",
            description = "Позволяет получить перечень ресторанов и количество отданных голосов"
    )
    @GetMapping("/voting_result")
    public List<VotingResult> getVotingResult(){
        log.info("get voting results");
        List<Menu> menusAvailableToday = menuService.findAllTodayAvailable();
        return menusAvailableToday.stream().map(menu -> {
            VotingResult votingResult = new VotingResult();
            votingResult.setRestaurantDTO(RestaurantDTO.convertToRestaurantDTO(menu.getOwnRestaurant()));
            votingResult.setVoteCount(voteService.countByMenu(menu));
            return votingResult;
        }).collect(Collectors.toList());
    }
}
