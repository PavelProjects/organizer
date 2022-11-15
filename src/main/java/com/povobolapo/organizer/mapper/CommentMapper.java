package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.comment.CommentDto;
import com.povobolapo.organizer.model.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses ={UserMapper.class, TaskMapper.class})
public interface CommentMapper {
    @Mapping(target = "task", source = "task.id")
    CommentDto toDto(CommentEntity entity);
    @Mapping(target = "task.id", source = "task")
    CommentEntity toEntity(CommentDto dto);
}
