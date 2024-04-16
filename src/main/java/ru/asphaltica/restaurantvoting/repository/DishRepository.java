package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asphaltica.restaurantvoting.model.Dish;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
}
