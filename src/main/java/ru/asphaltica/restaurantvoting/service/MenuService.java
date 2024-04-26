package ru.asphaltica.restaurantvoting.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.to.DishDto;
import ru.asphaltica.restaurantvoting.to.MenuDto;
import ru.asphaltica.restaurantvoting.to.RestaurantDto;
import ru.asphaltica.restaurantvoting.exceptions.EntityNotFoundException;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional (readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MenuService(MenuRepository menuRepository, ModelMapper modelMapper) {
        this.menuRepository = menuRepository;
        this.modelMapper = modelMapper;
    }

    public List<Menu> findAll(){
        return menuRepository.findAll();
    }

    @Cacheable(value = "MenusAvailable", unless = "#result == null")
    public List<Menu> findAllTodayAvailable(){
        return menuRepository.findByCreateDateIsBetween(DateTimeUtil.atStartOfToday(), DateTimeUtil.atEndOfVoting());
    }

    public Menu findById(int id){
        return menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Menu with this id wasn't found"));
    }

    public Menu getAvailableMenu(int restaurantId) {
        Restaurant onwRestaurant = new Restaurant();
        onwRestaurant.setId(restaurantId);
        return menuRepository
                .findByOwnRestaurantAndCreateDateBetween(onwRestaurant,
                        DateTimeUtil.atStartOfToday(),
                        DateTimeUtil.atEndOfVoting())
                .orElseThrow(() -> new EntityNotFoundException("this restaurant has no menus available today"));
    }

    public List<Menu> findAllByOwnRestaurantId(int restaurantId) {
        return menuRepository.findAllByOwnRestaurantId(restaurantId);
    }

    @Transactional
    public MenuDto create(Menu menu) {
        return convertToMenuDTO(menuRepository.save(menu));
    }

    @Transactional
    public void delete(int id) {
        findById(id);
        menuRepository.deleteById(id);
    }

    @Transactional
    public void update(Menu menu) {
        Menu updated = findById(menu.getId());
        menu.setCreateDate(updated.getCreateDate());
        menuRepository.save(menu);
    }

    private MenuDto convertToMenuDTO(Menu menu) {
        MenuDto menuDTO = new MenuDto();
        menuDTO.setId(menu.getId());
        menuDTO.setCreateDate(menu.getCreateDate());
        RestaurantDto restaurantDTO = modelMapper.map(menu.getOwnRestaurant(), RestaurantDto.class);
        menuDTO.setOwnRestaurant(restaurantDTO);
        List<DishDto> dishes = menu.getDishes().stream().map(dish -> modelMapper.map(dish, DishDto.class)).collect(Collectors.toList());
        menuDTO.setDishes(dishes);
        return menuDTO;
    }


}
