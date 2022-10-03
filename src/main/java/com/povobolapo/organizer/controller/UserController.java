package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.UserRequestBody;
import com.povobolapo.organizer.exception.ValidationException;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping("/create")
    public UserEntity createUser(@Valid @RequestBody UserRequestBody user) throws ValidationException {
        log.debug("POST-request: createUser (user={})", user);
        return userService.createUser(user);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUserByLogin (@RequestParam String login) {
        log.debug("DELETE-request: deleteUserByLogin (login={})", login);
        return userService.deleteUser(login);
    }
}
