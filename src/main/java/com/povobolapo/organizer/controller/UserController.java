package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserEntity getUserByLogin
            (@RequestParam String login) throws ValidationException {
        log.debug("GET-request: getUserByLogin (login={})", login);
        if (login == null) {
            log.error("Login is NULL.");
            throw new ValidationException("Login cannot be NULL.");
        }
        return userService.getUserByLogin(login);
    }

    //todo надо сделать авторизацию, а то у меня 403
    @PostMapping("/create")
    public UserEntity createUser(@RequestBody UserEntity user) throws ValidationException {
        log.debug("POST-request: createUser (user={})", user);
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Login is NULL.");
            throw new ValidationException("Login cannot be NULL.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            log.error("Password is NULL.");
            throw new ValidationException("Password cannot be NULL.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Set user.login as user.name.");
            user.setName(user.getLogin());
        }
        return userService.createUser(user);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUserByLogin (@RequestParam String login) {
        log.debug("GET-request: getUserByLogin (login={})", login);
        if (login == null) {
            return false;
        }
        return userService.deleteUser(login);
    }
}
