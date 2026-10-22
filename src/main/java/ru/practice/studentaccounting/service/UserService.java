package ru.practice.studentaccounting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practice.studentaccounting.model.User;
import ru.practice.studentaccounting.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String username, String password) {
        String normalizedUsername = username == null ? "" : username.trim();
        if (normalizedUsername.isBlank() || password == null || password.isBlank()
                || userRepository.findByUsername(normalizedUsername).isPresent()) {
            return false;
        }
        userRepository.save(new User(normalizedUsername, password));
        return true;
    }

    @Transactional(readOnly = true)
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username.trim())
                .filter(user -> user.getPassword().equals(password));
    }
}
