package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.notification.NotificationDto;
import com.povobolapo.organizer.model.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface NotificationMapper {
    NotificationDto toDto(NotificationEntity notificationEntity);
    NotificationEntity toEntity(NotificationDto notificationDto);
}
