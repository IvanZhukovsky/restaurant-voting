package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {
//    List<Menu> findAllByOwnRestaurantId(int id);
//    List<Menu> findByCreateDateIsBetween(LocalDateTime createDate, LocalDateTime createDate2);
//    boolean existsByOwnRestaurantAndCreateDateBetween(Restaurant ownRestaurant, LocalDateTime createDate, LocalDateTime createDate2);
//    Optional<Menu> findByOwnRestaurantAndCreateDateBetween(Restaurant ownRestaurant, LocalDateTime createDate, LocalDateTime createDate2);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.ownRestaurant ORDER BY m.ownRestaurant.id")
    List<Menu> findAllWithRestaurant();

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dishes WHERE m.ownRestaurant.id=?1")
    List<Menu> findAllByRestaurantIdWithDishes(int restaurantId);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.ownRestaurant LEFT JOIN FETCH m.dishes WHERE m.id=?1")
    Optional<Menu> findByIdWithRestaurantAndDishes(int id);
}
