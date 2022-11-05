package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.UserDto;
import com.povobolapo.organizer.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);
    UserEntity toEntity(UserDto userDto);
}
