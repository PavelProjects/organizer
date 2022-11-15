package com.povobolapo.organizer.controller.model.notification;

import com.povobolapo.organizer.controller.model.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private String id;
    private Date creationDate;
    private UserDto user;
    private UserDto creator;
    private String body;
    private boolean checked;
}
