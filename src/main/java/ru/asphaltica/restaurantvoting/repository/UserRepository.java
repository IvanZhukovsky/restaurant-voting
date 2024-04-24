package ru.asphaltica.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.model.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    Optional<User> findByEmailIgnoreCase(String email);
    void deleteByEmailIgnoreCase(String email);
}
