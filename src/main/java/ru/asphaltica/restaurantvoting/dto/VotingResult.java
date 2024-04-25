package ru.asphaltica.restaurantvoting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotingResult {

    private RestaurantDto restaurantDTO;
    private int voteCount;
}
