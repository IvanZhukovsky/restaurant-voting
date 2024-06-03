package ru.asphaltica.restaurantvoting.validation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.asphaltica.restaurantvoting.common.HasIdAndName;
import ru.asphaltica.restaurantvoting.repository.RestaurantRepository;

@Component
@AllArgsConstructor
public class RestaurantValidator implements Validator {

    public static final String EXCEPTION_DUPLICATE_RESTAURANT_NAME = "Restaurant with this name already exists";

    private final RestaurantRepository restaurantRepository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return HasIdAndName.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        HasIdAndName restaurant = (HasIdAndName) target;

        if (StringUtils.hasText(restaurant.getName())) {

            restaurantRepository.findByNameIgnoreCase(restaurant.getName()).ifPresent(
                    dbRestaurant -> {
                        if (request.getMethod().equals("PUT")) {  // UPDATE
                            int dbId = dbRestaurant.id();
                            // it is ok, if update ourselves
                            if (restaurant.getId() != null && dbId == restaurant.id()) return;
                            // Workaround for update with restaurant.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId))
                                return;
                        }
                        errors.rejectValue("name", "", EXCEPTION_DUPLICATE_RESTAURANT_NAME);
                    }
            );
        }
    }
}
