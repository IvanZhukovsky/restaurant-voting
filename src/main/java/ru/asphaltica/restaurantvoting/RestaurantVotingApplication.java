package ru.asphaltica.restaurantvoting;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.asphaltica.restaurantvoting.model.Role;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.repository.UserRepository;

import java.util.Set;

@SpringBootApplication
@AllArgsConstructor
public class RestaurantVotingApplication implements ApplicationRunner {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//		userRepository.save(new User("user@gmail.com", "User_First",
//                "User_last", "password", Set.of(Role.ROLE_USER)));
//		userRepository.save(new User("admin@javaops.ru",
//                "Admin_First", "Admin_last", "admin", Set.of(Role.ROLE_USER, Role.ROLE_ADMIN)));
//        System.out.println(userRepository.findAll());
    }
}
