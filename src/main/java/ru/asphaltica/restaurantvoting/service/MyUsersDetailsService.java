package ru.asphaltica.restaurantvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.asphaltica.restaurantvoting.config.MyUserDetails;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.repository.UserRepository;

import java.util.Optional;

@Service
public class MyUsersDetailsService implements UserDetailsService {


    private UserRepository userRepository;
    @Autowired
    public MyUsersDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MyUsersDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(username);
        return user.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
