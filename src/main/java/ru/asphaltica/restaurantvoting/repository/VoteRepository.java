package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asphaltica.restaurantvoting.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
}
