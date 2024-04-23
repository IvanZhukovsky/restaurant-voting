package ru.asphaltica.restaurantvoting.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.dto.DishDTO;
import ru.asphaltica.restaurantvoting.dto.MenuDTO;
import ru.asphaltica.restaurantvoting.dto.RestaurantDTO;
import ru.asphaltica.restaurantvoting.exceptions.EntityNotFoundException;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.Restaurant;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.repository.RestaurantRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional (readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository, ModelMapper modelMapper) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    public List<Menu> findAll(){
        List<Menu> menus = menuRepository.findAll();
        return menuRepository.findAll();
    }

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
    public MenuDTO create(Menu menu) {
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

    private MenuDTO convertToMenuDTO(Menu menu) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(menu.getId());
        menuDTO.setCreateDate(menu.getCreateDate());
        RestaurantDTO restaurantDTO = modelMapper.map(menu.getOwnRestaurant(), RestaurantDTO.class);
        menuDTO.setOwnRestaurant(restaurantDTO);
        List<DishDTO> dishes = menu.getDishes().stream().map(dish -> modelMapper.map(dish, DishDTO.class)).collect(Collectors.toList());
        menuDTO.setDishes(dishes);
        return menuDTO;
    }


}
