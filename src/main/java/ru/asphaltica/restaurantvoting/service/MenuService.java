package ru.asphaltica.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.common.error.NotFoundException;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.repository.RestaurantRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public List<Menu> findAll() {
        return menuRepository.findAllWithRestaurant();
    }

    //
//    @Cacheable(value = "MenusAvailable", unless = "#result == null")
//    public List<Menu> findAllTodayAvailable(){
//        return menuRepository.findByCreateDateIsBetween(DateTimeUtil.atStartOfToday(), DateTimeUtil.atEndOfVoting());
//    }
//
    public Menu findByIdWithRestaurantAndDishes(int id) {
        return menuRepository.findByIdWithRestaurantAndDishes(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    public Menu findById(int id) {
        return menuRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    public List<Menu> findAllByOwnRestaurantId(int restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NotFoundException("Entity with id=" + restaurantId + " not found");
        }
        return menuRepository.findAllByRestaurantIdWithDishes(restaurantId);
    }
    @Transactional
    public Menu create(Menu menu) {
        return menuRepository.save(menu);
    }

    @Transactional
    public void delete(int id) {
        findById(id);
        menuRepository.deleteById(id);
    }

    @Transactional
    public void update(Menu menu) {
        menuRepository.findByIdWithRestaurantAndDishes(menu.getId());
        menuRepository.save(menu);
    }
}
