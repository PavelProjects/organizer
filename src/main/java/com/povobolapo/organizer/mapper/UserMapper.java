package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.user.UserDto;
import com.povobolapo.organizer.model.UserEntity;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);
    UserEntity toEntity(UserDto userDto);

    Set<UserEntity> setDtoToEntity(Set<UserDto> participants);
    Set<UserDto> setEntityToDto(Set<UserEntity> participants);
}
