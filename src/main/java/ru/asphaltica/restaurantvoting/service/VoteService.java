package ru.asphaltica.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Vote;
import ru.asphaltica.restaurantvoting.repository.VoteRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    //private final VoteRepository voteRepository;

//    @Transactional
//    public String create(Vote vote) {
//        if (DateTimeUtil.isVotePeriod()) {
//            Optional<Vote> updated = voteRepository.findByUser(vote.getUser());
//            if (updated.isPresent()) {
//                voteRepository.delete(updated.get());
//                voteRepository.save(vote);
//            } else {
//                voteRepository.save(vote);
//            }
//            return "Вы успешно проголосовали";
//        }
//        return "На сегодня голосование завершено";
//    }
//
//    public List<Vote> findAllToday() {
//        return voteRepository.findAllByCreateDateIsBetween(DateTimeUtil.atStartOfToday(), DateTimeUtil.atEndOfVoting()).orElse(null);
//    }
//
//    public int countByMenu(Menu menu) {
//        return voteRepository.countAllByMenuAndCreateDateBetween(menu, DateTimeUtil.atStartOfToday(), DateTimeUtil.atEndOfVoting()).orElse(0);
//    }


}
