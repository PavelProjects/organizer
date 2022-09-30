package com.povobolapo.organizer.controller.models;

import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.repository.UserRepository;
import com.povobolapo.organizer.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {ObjectIdMapper.class})
public abstract class UserViewMapper {

    @Autowired
    private UserRepository userService;

    public abstract UserView toUserView(UserEntity user);

    public abstract List<UserView> toUserView(List<UserEntity> users);

    public UserView toUserViewById(int id) {
        if (id < 1) {
            return null;
        }
        return toUserView(userService.getById(id));
    }

}