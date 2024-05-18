package ru.asphaltica.restaurantvoting.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.asphaltica.restaurantvoting.common.HasId;
import ru.asphaltica.restaurantvoting.common.model.BaseEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "available_date"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends BaseEntity implements HasId {

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant ownRestaurant;

    @ElementCollection
    @CollectionTable(name = "menu_dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "name"})})
    @NotNull
    @NotEmpty
    private Set<Dish> dishes = new HashSet<>();

    public Menu(Integer id, LocalDate availableDate, Restaurant ownRestaurant, Set<Dish> dishes) {
        super(id);
        this.availableDate = availableDate;
        this.ownRestaurant = ownRestaurant;
        this.dishes = dishes;
    }
}
