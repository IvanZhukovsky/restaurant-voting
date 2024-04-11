package ru.asphaltica.restaurantvoting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table (name = "restaurant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(max = 128)
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "ownRestaurant")
    private List<Menu> menus;

    @OneToMany(mappedBy = "ownRestaurant")
    private List<Dish> dishes;
}
