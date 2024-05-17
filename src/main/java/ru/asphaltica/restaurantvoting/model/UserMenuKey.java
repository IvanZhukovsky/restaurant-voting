package ru.asphaltica.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

//@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMenuKey implements Serializable {
    @Column(name = "user_id")
    private int userId;

    @Column(name = "menu_id")
    private int menuId;
}
