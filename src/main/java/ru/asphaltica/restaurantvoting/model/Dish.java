package ru.asphaltica.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.asphaltica.restaurantvoting.util.JsonUtil;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dish {

    @Column(nullable = false)
    @NotEmpty
    @Size(max = 128)
    private String name;

    @Column(nullable = false)
    //@Column(nullable = false, precision=10, scale=2)
    @NotNull
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dish dish)) return false;
        return Objects.equals(getName(), dish.getName()) && Objects.equals(getPrice(), dish.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice());
    }

    @Override
    public String toString() {
        return JsonUtil.writeValue(this);
    }
}
