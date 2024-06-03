package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.user WHERE v.createdAt=?1 AND v.user=?2")
    Optional<Vote> findByCreatedAtAndAndUser(LocalDate createdAt, User user);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.user LEFT JOIN FETCH v.menu m " +
            "LEFT JOIN FETCH m.ownRestaurant WHERE v.id=?1")
    Optional<Vote> findById(int id);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.user u LEFT JOIN FETCH v.menu m " +
            "LEFT JOIN FETCH  m.dishes LEFT JOIN FETCH m.ownRestaurant WHERE u.email = ?1 AND v.createdAt=?2")
    Optional<Vote> findByUserEmailToday(String email, LocalDate today);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.user u LEFT JOIN FETCH v.menu m " +
            "LEFT JOIN FETCH  m.dishes LEFT JOIN FETCH m.ownRestaurant WHERE u.email = ?1")
    Optional<List<Vote>> findAllByUserEmail(String email);
}
