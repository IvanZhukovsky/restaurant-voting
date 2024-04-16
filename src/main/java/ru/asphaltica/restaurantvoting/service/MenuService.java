package ru.asphaltica.restaurantvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.repository.RestaurantRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

import java.util.List;

@Service
@Transactional (readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<Menu> findAll(){
        return menuRepository.findAll();
    }

    public List<Menu> findAllTodayAvailable(){
        return menuRepository.findByCreateDateIsBetween(DateTimeUtil.atStartOfToday(), DateTimeUtil.atEndOfVoting());
    }

    public Menu findById(int id){
        return menuRepository.findById(id).orElse(null);
    }

    public List<Menu> findAllByOwnRestaurantId(int restaurantId) {
        return menuRepository.findAllByOwnRestaurantId(restaurantId);
    }

    @Transactional
    public void create(int restaurantId) {
        Restaurant ownRestaurant = restaurantRepository.findById(restaurantId).orElse(null);
        Menu menu = new Menu();
        menu.setOwnRestaurant(ownRestaurant);
        menuRepository.save(menu);
    }

    @Transactional
    public void delete(int id) {
        menuRepository.deleteById(id);
    }

    @Transactional
    public void update(Menu menu) {
        menuRepository.save(menu);
    }



}
