package com.povobolapo.organizer.controller.model.task;

import com.povobolapo.organizer.controller.model.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDto {

    private String id;
    private String name;
    private String description;
    private String status;
    private Date creationDate;
    private Date deadline;
    private UserDto author;
    private Set<UserDto> participants;
}
