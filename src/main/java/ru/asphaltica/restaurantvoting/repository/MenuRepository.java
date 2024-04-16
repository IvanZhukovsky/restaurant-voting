package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.asphaltica.restaurantvoting.model.Menu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findAllByOwnRestaurantId(int id);
    List<Menu> findByCreateDateIsBetween(LocalDateTime createDate, LocalDateTime createDate2);

}
