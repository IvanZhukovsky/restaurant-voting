package ru.asphaltica.restaurantvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.Dish;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.repository.DishRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DishService {
    private final DishRepository dishRepository;

    @Autowired
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> findAll(){
        return dishRepository.findAll();
    }

    public Dish findById(int id){
        return dishRepository.findById(id).orElse(null);
    }

    public List<Dish> findAllByOwnRestaurantId(int restaurantId) {
        return dishRepository.findAllByOwnRestaurantId(restaurantId);
    }

    @Transactional
    public Dish create(Dish dish) {
        return dishRepository.save(dish);
    }

    @Transactional
    public void deleteById(int id) {
        dishRepository.deleteById(id);
    }

    @Transactional
    public void update(Dish dish) {
        dishRepository.save(dish);
    }


}
