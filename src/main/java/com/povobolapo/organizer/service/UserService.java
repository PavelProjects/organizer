package com.povobolapo.organizer.service;


import com.povobolapo.organizer.controller.models.UserRequestBody;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.exception.ValidationException;
import com.povobolapo.organizer.repository.UserRepository;
import com.povobolapo.organizer.model.UserEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(UserRequestBody userBody) {
        UserEntity user = userBody.toUser();
        String encodedPassword = encodePassword(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        user.setPassword(null);
        return user;
    }

    public boolean deleteUser(String login) throws ValidationException {
            UserEntity user = userRepository.findByLogin(login);
            if (user == null) {
                throw new NotFoundException("User with login [" + login + "] not found");
            }
            log.debug("Found user: " + user);
            userRepository.delete(user);
        return true;
    }

    public UserEntity getUserByLogin(String login) {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            throw new NotFoundException("User with login [" + login + "] not found");
        }
        return user;
    }
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
