package ru.asphaltica.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.common.error.NotFoundException;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    @Transactional
    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(int id){
        findById(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteByMail(String email) {
        findByMail(email);
        userRepository.deleteByEmailIgnoreCase(email);
    }

    @Transactional
    public void update(User newUser) {
        User oldUser = findById(newUser.getId());

        if (newUser.getPassword() == null) {
            newUser.setPassword(oldUser.getPassword());
        } else {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        userRepository.save(newUser);
    }

    public User findByMail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }


}
