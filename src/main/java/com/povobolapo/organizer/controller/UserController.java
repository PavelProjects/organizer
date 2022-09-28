package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


}
