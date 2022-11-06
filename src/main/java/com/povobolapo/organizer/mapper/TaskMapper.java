package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.task.TaskDto;
import com.povobolapo.organizer.controller.model.task.TaskRequestBody;
import com.povobolapo.organizer.model.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses ={UserMapper.class})
public interface TaskMapper {
    // Передаем на фронт только caption значение статуса

    @Mapping(target = "status", source = "dictTaskStatus.caption")
    TaskDto toDto(TaskEntity task);
    TaskEntity toEntity(TaskDto taskDto);

    @Mapping(target = "author.login", source = "author")
    @Mapping(target = "participants", ignore = true)
    TaskEntity toEntity(TaskRequestBody taskRequestBody);

    @Named("DtoWithoutDesc")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "status", source = "dictTaskStatus.caption")
    TaskDto toDtoShort(TaskEntity task);
}
