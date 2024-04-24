package ru.asphaltica.restaurantvoting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.asphaltica.restaurantvoting.model.Role;

import java.util.Set;

@Getter
@Setter
public class UserDTO {

    @Email(message = "format must match email address")
    @NotEmpty(message = "email address must be specified")
    @Size(max = 128, message = "length should not exceed 128 characters")
    private String email;

    @Size(max = 128, message = "length should not exceed 128 characters")
    private String firstName;

    @Size(max = 128, message = "length should not exceed 128 characters")
    private String lastName;

    @Size(max = 256, message = "length should not exceed 256 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Set<Role> roles;

}
