package com.povobolapo.organizer.controller.model.task;

import com.povobolapo.organizer.controller.groups.OnCreate;
import com.povobolapo.organizer.controller.groups.OnUpdate;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TaskRequestBody {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private String id;
    @Size(max = 128)
    @NotNull(groups = OnCreate.class)
    private String name;

    private String description;

    @Size(max = 32)
    @NotNull(groups = OnCreate.class)
    private String author;

    @Size(max = 32)
    private String status;

    private Date deadline;
    private Set<String> participants = new HashSet<>();

    public TaskRequestBody(String name, String description, String author) {
        this.name = name;
        this.description = description;
        this.author = author;
    }

    public TaskRequestBody(String id, String description) {
        this.id = id;
        this.description = description;
    }
}
