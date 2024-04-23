package ru.asphaltica.restaurantvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.exceptions.EntityNotFoundException;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.repository.RestaurantRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> findAll(){
        return restaurantRepository.findAll();
    }

    public Restaurant findById(int id){
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with this id wasn't found"));
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public Restaurant update(int id, Restaurant restaurant) {
        checkExistById(id);
        restaurant.setId(id);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void delete(int id) {
        checkExistById(id);
        restaurantRepository.deleteById(id);
    }

    private void checkExistById(int id) {
        if (!restaurantRepository.existsById(id)) throw new EntityNotFoundException("Restaurant with this id wasn't found");
    }

}
