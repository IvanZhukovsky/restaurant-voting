package ru.asphaltica.restaurantvoting.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.asphaltica.restaurantvoting.common.HasIdAndEmail;
import ru.asphaltica.restaurantvoting.common.model.BaseEntity;
import ru.asphaltica.restaurantvoting.model.Role;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserDto extends BaseEntity implements HasIdAndEmail {

    @Email(message = "format must match email address")
    @NotEmpty(message = "email address must be specified")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String email;

    @NotEmpty(message = "first name must not be empty")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String firstName;

    @NotEmpty(message = "last name must not be empty")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String lastName;


    @Size(max = 256, message = "length should not exceed 256 characters")
    private String password;

    private Set<Role> roles;
}
