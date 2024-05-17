package ru.asphaltica.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import ru.asphaltica.restaurantvoting.common.HasIdAndEmail;
import ru.asphaltica.restaurantvoting.common.model.BaseEntity;

import java.util.EnumSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //Потому что нужен только для Hibernate
@ToString(callSuper = true, exclude = {"password"})
public class User extends BaseEntity implements HasIdAndEmail {
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "format must match email address")
    @NotEmpty(message = "email address must be specified")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String email;

    @Column(name = "first_name")
    @NotBlank(message = "first name must not be empty")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "last name must not be empty")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String lastName;

    @Column(name = "password")
    @Size(max = 256, message = "length should not exceed 256 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    private Set<Role> roles = EnumSet.noneOf(Role.class);

    public User(Integer id, String email, String firstName, String lastName, String password, Set<Role> roles) {
        super(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
    }

    public User (User u) {
        this(u.id, u.email, u.firstName, u.lastName, u.password, u.roles);
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

}
