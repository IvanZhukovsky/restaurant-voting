package ru.asphaltica.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "name", nullable = false, unique = true)
    @Size(max = 128, message = "the length of the restaurant name should not exceed 128 characters")
    @NotEmpty(message = "the restaurant name must not be empty")
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "ownRestaurant")
    private List<Menu> menus;

    @JsonIgnore
    @OneToMany(mappedBy = "ownRestaurant")
    private List<Dish> dishes;

    public Restaurant(String name) {
    }
}
