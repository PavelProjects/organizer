package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.TaskDto;
import com.povobolapo.organizer.controller.model.TaskRequestBody;
import com.povobolapo.organizer.model.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses ={UserMapper.class})
public interface TaskMapper {
    @Mappings({
            @Mapping(target = "status", source = "dictTaskStatus.caption"),
    })
    TaskDto toDto(TaskEntity task);
    TaskEntity toEntity(TaskDto taskDto);
    @Mappings({
            @Mapping(target = "author.login", source = "author")
    })
    TaskEntity toEntity(TaskRequestBody taskRequestBody);
}
