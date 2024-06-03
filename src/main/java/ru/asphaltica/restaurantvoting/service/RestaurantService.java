package ru.asphaltica.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.common.error.NotFoundException;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public List<Restaurant> findAll(){
        return restaurantRepository.findAll();
    }

    public Restaurant findById(int id){
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
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

    public List<Restaurant> findAllTodayAvailable(){
        List<Menu> menus =  menuRepository.findAllTodayAvailable(LocalDate.now()).get();
        if (menus.isEmpty()) {
            throw new NotFoundException("no restaurants available for");
        }
        return menus.stream().map(Menu::getOwnRestaurant).toList();
    }

    private void checkExistById(int id) {
        if (!restaurantRepository.existsById(id)) throw new NotFoundException("Entity with id=" + id + " not found");
    }
}
