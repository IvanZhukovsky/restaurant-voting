package ru.asphaltica.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //Потому что нужен только для Hibernate
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"password"})
public class User extends BaseEntity {
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "format must match email address")
    @NotEmpty(message = "email address must be specified")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String email;

    @Column(name = "first_name")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String firstName;

    @Column(name = "last_name")
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
    private Set<Role> roles;
}
