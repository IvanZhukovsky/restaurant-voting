package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
//    Optional<List<Vote>> findAllByCreateDateIsBetween(LocalDateTime createDate1, LocalDateTime createDate2);
//    Optional<Integer> countAllByMenuAndCreateDateBetween(Menu menu, LocalDateTime createDate1, LocalDateTime createDate2);
//    Optional<Vote> findByUser(User user);

    Optional<Vote> findByCreatedAtAndAndUser(LocalDate createdAt, User user);
}
