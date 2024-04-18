package ru.asphaltica.restaurantvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.Vote;
import ru.asphaltica.restaurantvoting.repository.VoteRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

@Service
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Transactional
    public String create(Vote vote) {
        if (DateTimeUtil.isVotePeriod()) {
            voteRepository.save(vote);
            return "Вы успешно проголосавали";
        }
        return "На сегодня голосование завершено";
    }


}
