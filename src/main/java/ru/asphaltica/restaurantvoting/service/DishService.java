package ru.asphaltica.restaurantvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.exceptions.EntityNotFoundException;
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
        return dishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Dish with this id wasn't found"));
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
        checkExistById(id);
        dishRepository.deleteById(id);
    }

    @Transactional
    public void update(Dish dish) {
        Dish updated = dishRepository.findById(dish.getId()).orElseThrow(() -> new EntityNotFoundException("Dish with this id wasn't found"));
        dish.setOwnRestaurant(updated.getOwnRestaurant());
        dishRepository.save(dish);
    }

    private void checkExistById(int id) {
        if (!dishRepository.existsById(id)) throw new EntityNotFoundException("Dish with this id wasn't found");
    }


}
