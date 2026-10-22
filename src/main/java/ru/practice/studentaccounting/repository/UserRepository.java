package ru.practice.studentaccounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practice.studentaccounting.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
