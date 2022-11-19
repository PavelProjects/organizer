package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.user.UserRelationDto;
import com.povobolapo.organizer.model.UserRelationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserRelationMapper {
    UserRelationDto toDto(UserRelationEntity userRelationEntity);
}
