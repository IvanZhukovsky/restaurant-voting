package ru.asphaltica.restaurantvoting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.dto.RestaurantDTO;
import ru.asphaltica.restaurantvoting.dto.VotingResult;
import ru.asphaltica.restaurantvoting.model.*;
import ru.asphaltica.restaurantvoting.service.MenuService;
import ru.asphaltica.restaurantvoting.service.VoteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/votes")
public class VoteController {

    private final VoteService voteService;
    private final MenuService menuService;



    @Autowired
    public VoteController(VoteService voteService, MenuService menuService) {
        this.voteService = voteService;
        this.menuService = menuService;
    }

    @GetMapping
    public List<Vote> findAllToday(){
        return voteService.findAllToday();
    }

    @GetMapping("/voting_result")
    public List<VotingResult> getVotingResult(){
        List<Menu> menusAvailableToday = menuService.findAllTodayAvailable();
        return menusAvailableToday.stream().map(menu -> {
            VotingResult votingResult = new VotingResult();
            votingResult.setRestaurantDTO(RestaurantDTO.convertToRestaurantDTO(menu.getOwnRestaurant()));
            votingResult.setVoteCount(voteService.countByMenu(menu));
            return votingResult;
        }).collect(Collectors.toList());
    }



}
