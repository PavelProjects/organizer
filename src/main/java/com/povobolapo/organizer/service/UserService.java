package com.povobolapo.organizer.service;


import com.povobolapo.organizer.controller.model.UserRequestBody;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.exception.ValidationException;
import com.povobolapo.organizer.repository.UserRepository;
import com.povobolapo.organizer.model.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UserEntity createUser(UserRequestBody userBody) {
        UserEntity user = userRepository.findByLogin(userBody.getLogin());
        if (user != null) {
            log.info("User already exists, skipping creation");
            return user;
        }
        // Валидация здесь, а не в объекте, тк RequestBody используется в методе апдейта,
        // где пароль может быть null
        if (StringUtils.isBlank(userBody.getPassword())) {
            throw new ValidationException("Password is missing!");
        }

        user = userBody.toUser();
        user.setPassword(encodePassword(userBody.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void updateUser(UserRequestBody userBody) {
        // Менять можно только свою учетку
        if (!canUpdateUser(userBody.getLogin())) {
            throw new AccessDeniedException("Permission denied!");
        }

        UserEntity user = userRepository.findByLogin(userBody.getLogin());
        if (user == null) {
            throw new NotFoundException("User with login [" + userBody.getLogin() + "] not found");
        }

        if (StringUtils.isNotBlank(userBody.getPassword())) {
            user.setPassword(encodePassword(userBody.getPassword()));
        }
        if (StringUtils.isBlank(userBody.getName())) {
            user.setName(userBody.getName());
        }
        if (StringUtils.isNotBlank(userBody.getAvatar())) {
            user.setAvatar(userBody.getAvatar());
        }

    }

    @Transactional
    public void deleteUser(String login) throws ValidationException {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            throw new NotFoundException("User with login [" + login + "] not found");
        }

        // Можно удалить только свою учетку
        // Если учетку пытается удалить другой юзер кидаем ошибку
        if (!canUpdateUser(login)) {
            throw new AccessDeniedException("Permission denied!");
        }

        userRepository.delete(user);
    }

    public UserEntity getUserByLogin(String login) {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            throw new NotFoundException("User with login [" + login + "] not found");
        }
        return user;
    }

    // Проверяет, может ли текущий юзер менять юзера
    private boolean canUpdateUser(String userLoginToChange) {
        String currentUser = authenticatedUserName();
        log.warn(String.format("User %s trying to edit user %s", currentUser, userLoginToChange));
        return currentUser.equals(userLoginToChange);
    }

    private String authenticatedUserName() {
        // Получем из контекста безопасности какой юзер сейчас делает запрос
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        Objects.requireNonNull(currentUser, "Current user didn't authenticated!");
        return currentUser.getName();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
