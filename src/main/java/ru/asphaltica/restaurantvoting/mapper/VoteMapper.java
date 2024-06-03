package ru.asphaltica.restaurantvoting.mapper;

import org.springframework.stereotype.Component;
import ru.asphaltica.restaurantvoting.model.Vote;
import ru.asphaltica.restaurantvoting.to.VoteDto;

@Component
public class VoteMapper {

    public VoteDto convertToVoteDto(Vote vote){
        VoteDto voteDto = new VoteDto();
        voteDto.setId(vote.getId());
        voteDto.setCreatedAt(vote.getCreatedAt());
        voteDto.setMenu(vote.getMenu());
        return voteDto;
    }
}
