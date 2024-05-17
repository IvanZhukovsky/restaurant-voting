package ru.asphaltica.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.common.HasIdAndName;
import ru.asphaltica.restaurantvoting.common.model.BaseEntity;
import ru.asphaltica.restaurantvoting.model.Restaurant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto extends BaseEntity implements HasIdAndName {

    @NotBlank
    private String name;
}
