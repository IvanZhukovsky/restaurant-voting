package ru.asphaltica.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.asphaltica.restaurantvoting.mapper.VoteMapper;
import ru.asphaltica.restaurantvoting.model.Vote;
import ru.asphaltica.restaurantvoting.service.VoteService;
import ru.asphaltica.restaurantvoting.to.VoteDto;
import ru.asphaltica.restaurantvoting.util.URIUtil;

import java.util.List;

import static ru.asphaltica.restaurantvoting.controller.VoteController.REST_URL;

@Tag(name = "Получение данных о голосовании",
        description = "Позволяет пользователям получать текущие результаты голосования за рестораны")
@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@Slf4j
@AllArgsConstructor
public class VoteController {

    public static final String REST_URL = "/api/votes";

    private final VoteService voteService;
    private final VoteMapper voteMapper;

    @Operation(
            summary = "Получение информации о голосе по его id",
            description = "Позволяет администратору получить данные о голосе по его id"
    )
    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        log.info("get vote with id = {}", id);
        return voteService.findById(id);
    }

    @Operation(
            summary = "Создание голоса",
            description = "Позволяет пользователю проголосовать за ресторан по его id"
    )
    @Validated
    @PostMapping
    public ResponseEntity<Vote> create(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam @NotNull int restaurantId) {
        log.info("create new vote");
        Vote created = voteService.create(userDetails.getUsername(), restaurantId);
        log.info("A vote has been created with id = {}", created.getId());
        return ResponseEntity.created(URIUtil.getCreatedUri("/api/votes/{id}", created.getId())).body(created);
    }

    @Operation(
            summary = "Получение информации о голосовании за сегодня",
            description = "Позволяет пользователю получить информацию о его голосовании за сегодня"
    )
    @GetMapping("/profile-today")
    public VoteDto getMyVotingToday(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("get voting today");
        return voteMapper.convertToVoteDto(voteService.findByUserEmailToday(userDetails.getUsername()));
    }

    @Operation(
            summary = "История голосования",
            description = "Позволяет пользователю получить всю историю его голосования"
    )
    @GetMapping("/profile-all")
    public List<VoteDto> getMyVotingHistory(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("get voting history");
        return voteService.findAllByUserEmail(userDetails.getUsername())
                .stream().map(voteMapper::convertToVoteDto).toList();
    }
}
