package com.povobolapo.organizer.controller.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private String login;
    private String name;
    private String avatar;

}
