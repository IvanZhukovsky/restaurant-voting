package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findAllByOwnRestaurantId(int id);
    Optional<Dish> findByName(String name);
}
