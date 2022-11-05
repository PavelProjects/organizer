package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.CommentDto;
import com.povobolapo.organizer.model.CommentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses ={UserMapper.class, TaskMapper.class})
public interface CommentMapper {
    CommentDto toDto(CommentEntity entity);
    CommentEntity toEntity(CommentDto dto);
}
