package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findAllByOwnRestaurantId(int id);
    List<Menu> findByCreateDateIsBetween(LocalDateTime createDate, LocalDateTime createDate2);
    boolean existsByOwnRestaurantAndCreateDateBetween(Restaurant ownRestaurant, LocalDateTime createDate, LocalDateTime createDate2);
    Optional<Menu> findByOwnRestaurantAndCreateDateBetween(Restaurant ownRestaurant, LocalDateTime createDate, LocalDateTime createDate2);

}
