package ru.asphaltica.restaurantvoting.mapper;

import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.dto.UserDto;
import ru.asphaltica.restaurantvoting.model.User;

public class UserMapper {

    private UserMapper() {
    }

    private static  ModelMapper modelMapper = new ModelMapper();

    public static User convertToUser(UserDto userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public static UserDto convertToUserDTO(User user) {
        return modelMapper.map(user, UserDto.class);
    }

}
