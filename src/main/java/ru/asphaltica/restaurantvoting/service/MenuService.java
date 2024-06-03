package ru.asphaltica.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.common.error.NotFoundException;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final CacheManager cacheManager;


    public List<Menu> findAll() {
        return menuRepository.findAllWithRestaurant();
    }

    public Menu findByIdWithRestaurantAndDishes(int id) {
        return menuRepository.findByIdWithRestaurantAndDishes(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    @Cacheable(value ="menu")
    public Menu findByRestaurantIdAvailableToday(int restaurantId) {
        return menuRepository.findByAvailableDateAndOwnRestaurantWithDishes(LocalDate.now(), restaurantId)
                .orElseThrow(() -> new NotFoundException("At the restaurant id=" + restaurantId + " there are no menus available for today"));
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

    @CacheEvict(value = "menu", allEntries = true)
    @Transactional
    public void delete(int id) {
        findById(id);
        menuRepository.deleteById(id);

    }

    @CacheEvict(value = "menu", allEntries = true)
    @Transactional
    public void update(Menu menu) {

        Menu toBeUpdated = menuRepository.findByIdWithRestaurantAndDishes(menu.getId())
                .orElseThrow(() -> new NotFoundException("Entity with id=" + menu.getId() + " not found"));
        menu.setAvailableDate(toBeUpdated.getAvailableDate());
        menuRepository.save(menu);
    }
}
