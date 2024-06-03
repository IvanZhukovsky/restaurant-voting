package ru.asphaltica.restaurantvoting.to;

import lombok.Getter;
import lombok.Setter;
import ru.asphaltica.restaurantvoting.model.Menu;

import java.time.LocalDate;

@Getter
@Setter
public class VoteDto {
    private int id;
    private LocalDate createdAt;
    private Menu menu;
}
