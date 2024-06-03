package ru.asphaltica.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.asphaltica.restaurantvoting.common.HasId;
import ru.asphaltica.restaurantvoting.common.model.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"created_at", "user_id"})})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vote extends BaseEntity implements HasId {

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Menu menu;

    public Vote(Integer id, LocalDate createdAt, User user, Menu menu) {
        super(id);
        this.createdAt = createdAt;
        this.user = user;
        this.menu = menu;
    }
}
