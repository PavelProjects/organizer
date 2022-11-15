package com.povobolapo.organizer.service;


import com.povobolapo.organizer.controller.model.user.UserRequestBody;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.exception.ValidationException;
import com.povobolapo.organizer.model.UserCreditsEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.repository.UserCreditsRepository;
import com.povobolapo.organizer.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;


@Service
@Scope("singleton")
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserCreditsRepository userCreditsRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContentService contentService;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserCreditsRepository userCreditsRepository,
                       PasswordEncoder passwordEncoder,
                       ContentService contentService) {
        this.userRepository = userRepository;
        this.userCreditsRepository = userCreditsRepository;
        this.passwordEncoder = passwordEncoder;
        this.contentService = contentService;
    }

    @Transactional
    public UserEntity createUser(UserRequestBody userBody) {
        UserEntity user = userRepository.findByLogin(userBody.getLogin());
        if (user != null) {
            log.info("User already exists, skipping creation");
            return user;
        }
        // Сначала создаем приватные данные
        createUserCredits(userBody);
        // Далее публичные
        user = userBody.toUser();
        userRepository.save(user);
        return user;
    }

    private void createUserCredits(UserRequestBody userBody) {
        // Валидация здесь, а не в объекте, тк RequestBody используется в методе апдейта,
        // где пароль может быть null
        if (StringUtils.isBlank(userBody.getPassword())) {
            throw new ValidationException("Password is missing!");
        }

        UserCreditsEntity userCreditsEntity = new UserCreditsEntity(
                userBody.getLogin(),
                encodePassword(userBody.getPassword()),
                userBody.getMail()
        );
        userCreditsRepository.save(userCreditsEntity);
    }

    @Transactional
    public void updateUser(UserRequestBody userBody) throws AuthenticationException {
        // Менять можно только свою учетку
        if (!UserAuthoritiesService.canModifyUserData(userBody.getLogin())) {
            throw new AccessDeniedException("Permission denied!");
        }

        UserEntity user = userRepository.findByLogin(userBody.getLogin());
        if (user == null) {
            throw new NotFoundException("User with login [" + userBody.getLogin() + "] not found");
        }

        if (StringUtils.isBlank(userBody.getName())) {
            user.setName(userBody.getName());
        }
        if (StringUtils.isNotBlank(userBody.getAvatar())) {
            user.setAvatar(contentService.getContentInfo(userBody.getAvatar()));
        }

        log.info("User {} was updated by {}", userBody.getLogin(), UserAuthoritiesService.getCurrentUserLogin());
    }

    @Transactional
    public void deleteUser(String login) throws ValidationException, AuthenticationException {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            throw new NotFoundException("User with login [" + login + "] not found");
        }

        // Можно удалить только свою учетку
        // Если учетку пытается удалить другой юзер кидаем ошибку
        if (!UserAuthoritiesService.canDeleteUserData(login)) {
            throw new AccessDeniedException("Permission denied!");
        }

        userRepository.delete(user);
        log.warn("User {} was deleted by {}", login, UserAuthoritiesService.getCurrentUserLogin());
    }

    public UserEntity getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public UserEntity getCurrentUser() throws AuthenticationException {
        return getUserByLogin(UserAuthoritiesService.getCurrentUserLogin());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
