package ru.asphaltica.restaurantvoting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class UsersResponse {
    private List<UserDTO> users;
}
