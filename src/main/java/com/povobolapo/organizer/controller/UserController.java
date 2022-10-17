package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.UserInfoResponse;
import com.povobolapo.organizer.controller.model.UserRequestBody;
import com.povobolapo.organizer.exception.ValidationException;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.service.UserService;
import com.povobolapo.organizer.utils.EventDispatcher;
import com.povobolapo.organizer.websocket.SpringContext;
import com.povobolapo.organizer.websocket.model.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
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

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getUserInfo(@RequestParam String login) throws ValidationException {
        log.debug("GET-request: getUserInfo (login={})", login);
        if (login == null || login.isEmpty()) {
            log.error("Login is empty");
            throw new ValidationException("Login can't be empty!");
        }
        try {
            SpringContext.getBean(EventDispatcher.class).dispatch(new NotificationMessage(userService.getUserByLogin(login), "test", "test body"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new UserInfoResponse(userService.getUserByLogin(login));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfoResponse createUser(@Valid @RequestBody UserRequestBody user) throws ValidationException {
        log.debug("POST-request: createUser (user={})", user);
        return new UserInfoResponse(userService.createUser(user));
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@Valid @RequestBody UserRequestBody user) throws ValidationException, AuthenticationException {
        log.debug("PUT-request: updateUser (user={})", user);
        userService.updateUser(user);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserByLogin (@RequestParam String login) throws AuthenticationException {
        log.debug("DELETE-request: deleteUserByLogin (login={})", login);
        userService.deleteUser(login);
    }
}