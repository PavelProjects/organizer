package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.user.UserDto;
import com.povobolapo.organizer.controller.model.user.UserRequestBody;
import com.povobolapo.organizer.controller.model.user.UserSearchRequest;
import com.povobolapo.organizer.exception.ValidationException;
import com.povobolapo.organizer.mapper.UserMapper;
import com.povobolapo.organizer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findUsers(@RequestBody(required = false) UserSearchRequest userSearchRequest) {
        if (userSearchRequest == null) {
            userSearchRequest = new UserSearchRequest();
        }
        return userService.searchForUsers(userSearchRequest).stream().map(userMapper::toDto).collect(Collectors.toList());
    }


    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserInfo(@RequestParam String login) throws ValidationException {
        log.debug("GET-request: getUserInfo (login={})", login);
        if (login == null || login.isEmpty()) {
            log.error("Login is empty");
            throw new ValidationException("Login can't be empty!");
        }
        return userMapper.toDto(userService.getUserByLogin(login));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRequestBody user) throws ValidationException {
        log.debug("POST-request: createUser (user={})", user);
        return userMapper.toDto(userService.createUser(user));
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