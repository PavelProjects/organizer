package com.povobolapo.organizer.service;


import com.povobolapo.organizer.repository.UserRepository;
import com.povobolapo.organizer.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(String login, String password, String name) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(password);
        Objects.requireNonNull(name);

        UserEntity user = new UserEntity(login, encodePassword(password), name);
        userRepository.save(user);
        user.setPassword(null);
        return user;
    }

    public boolean deleteUser(String login) {
        UserEntity user = userRepository.findByLogin(login);
        if (login == null || user.getId() < 1) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    public UserEntity getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
