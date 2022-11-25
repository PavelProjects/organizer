package com.povobolapo.organizer.controller.model.user;

import lombok.Data;

@Data
public class UserRelationDto {
    private UserDto firstUser;
    private UserDto secondUser;
    private String relationType;
}
