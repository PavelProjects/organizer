package com.povobolapo.organizer.controller.model.user;

import com.povobolapo.organizer.controller.model.ContentDto;
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
    private ContentDto avatar;

}
