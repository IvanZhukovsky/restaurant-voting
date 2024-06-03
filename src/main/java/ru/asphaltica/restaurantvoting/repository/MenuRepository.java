package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.ownRestaurant ORDER BY m.ownRestaurant.id")
    List<Menu> findAllWithRestaurant();

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dishes WHERE m.ownRestaurant.id=?1")
    List<Menu> findAllByRestaurantIdWithDishes(int restaurantId);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.ownRestaurant LEFT JOIN FETCH m.dishes WHERE m.id=?1")
    Optional<Menu> findByIdWithRestaurantAndDishes(int id);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.ownRestaurant WHERE m.availableDate=?1 AND m.ownRestaurant.id=?2")
    Optional<Menu> findByAvailableDateAndOwnRestaurant(LocalDate availableDate, int restaurantId);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.ownRestaurant LEFT JOIN FETCH m.dishes WHERE m.availableDate=?1 AND m.ownRestaurant.id=?2")
    Optional<Menu> findByAvailableDateAndOwnRestaurantWithDishes(LocalDate availableDate, int restaurantId);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.ownRestaurant WHERE m.availableDate=?1")
    Optional<List<Menu>> findAllTodayAvailable(LocalDate today);
}
