package com.povobolapo.organizer.controller.model.task;

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
public class TaskDto {
    private String id;
    private UserDto author;
    private String name;
    private String status;
    private Date deadline;
}
