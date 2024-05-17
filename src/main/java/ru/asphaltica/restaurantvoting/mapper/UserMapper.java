package ru.asphaltica.restaurantvoting.mapper;

import org.modelmapper.ModelMapper;
import ru.asphaltica.restaurantvoting.to.UserDto;
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

    public static User updateFromTo(User user, UserDto userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPassword(userDto.getPassword());
        return user;
    }

}
