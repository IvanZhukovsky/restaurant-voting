package ru.asphaltica.restaurantvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.exceptions.EntityNotFoundException;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with this id wasn't found"));
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
    public void update(User user) {
        findById(user.getId());
        userRepository.save(user);
    }

    public User findByMail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }


}
