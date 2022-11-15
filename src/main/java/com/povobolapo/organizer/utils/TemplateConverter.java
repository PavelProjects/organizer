package com.povobolapo.organizer.utils;

import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;

import javax.validation.constraints.NotNull;

public class TemplateConverter {
    public static final String USER_NAME_TEMPLATE_PARAMETER = "${userName}";
    public static final String TASK_NAME_TEMPLATE_PARAMETER = "${taskName}";

    public static String convertTemplate(@NotNull String template,@NotNull UserEntity userEntity,@NotNull TaskEntity task) {
        return template
                .replace(USER_NAME_TEMPLATE_PARAMETER, userEntity.getName())
                .replace(TASK_NAME_TEMPLATE_PARAMETER, task.getName());
    }
}
