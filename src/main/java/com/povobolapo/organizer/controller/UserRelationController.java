package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.user.UserRelationDto;
import com.povobolapo.organizer.controller.model.user.UserRelationSearch;
import com.povobolapo.organizer.mapper.UserRelationMapper;
import com.povobolapo.organizer.service.UserRelationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/relations")
@Log4j2
public class UserRelationController {
    private final UserRelationService userRelationService;
    private final UserRelationMapper relationMapper;

    public UserRelationController(UserRelationService userRelationService,
                                  UserRelationMapper userRelationMapper) {
        this.userRelationService = userRelationService;
        this.relationMapper = userRelationMapper;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<UserRelationDto> getRelations(@RequestBody(required = false) UserRelationSearch userRelationSearch) throws AuthenticationException {
        if (userRelationSearch == null) {
            userRelationSearch = new UserRelationSearch();
        }
        return userRelationService.getRelations(userRelationSearch).stream().map(relationMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendRelation(@RequestParam("login") String login) throws AuthenticationException {
        userRelationService.sendRelationRequest(login);
    }

    @PutMapping("/approve")
    @ResponseStatus(HttpStatus.CREATED)
    public void approveRelation(@RequestParam("relationId") String relationId) throws AuthenticationException {
        userRelationService.approveRelation(relationId);
    }

    @PutMapping("/decline")
    @ResponseStatus(HttpStatus.CREATED)
    public void declineRelation(@RequestParam("relationId") String relationId) throws AuthenticationException {
        userRelationService.declineRelation(relationId);
    }

    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.CREATED)
    public void removeRelation(@RequestParam("login") String login) throws AuthenticationException {
        userRelationService.removeRelation(login);
    }
}
