package ru.asphaltica.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.asphaltica.restaurantvoting.app.config.CustomUserDetails;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUsersDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
    }
}
