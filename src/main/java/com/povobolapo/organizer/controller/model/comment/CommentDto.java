package com.povobolapo.organizer.controller.model.comment;

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
public class CommentDto {
    private String id;
    private Date creationDate;
    private String body;
    private String task;
    private UserDto author;
}
