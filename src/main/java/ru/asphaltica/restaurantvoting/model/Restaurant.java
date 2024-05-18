package ru.asphaltica.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.asphaltica.restaurantvoting.common.HasId;
import ru.asphaltica.restaurantvoting.common.model.BaseEntity;

import java.util.List;
import java.util.Set;

@Entity
@Table (name = "restaurant")
@NoArgsConstructor
@Getter
@Setter
public class Restaurant extends BaseEntity implements HasId {

    @Column(name = "name", nullable = false, unique = true)
    @Size(max = 128, message = "the length of the restaurant name should not exceed 128 characters")
    @NotBlank(message = "the restaurant name must not be empty")
    private String name;

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
