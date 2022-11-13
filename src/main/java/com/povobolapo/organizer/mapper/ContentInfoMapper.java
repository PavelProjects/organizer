package com.povobolapo.organizer.mapper;

import com.povobolapo.organizer.controller.model.ContentDto;
import com.povobolapo.organizer.model.ContentInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ContentInfoMapper {
    @Mappings({
            @Mapping(target = "contentInfoId", source = "id"),
            @Mapping(target = "contentId", source = "content.id"),
    })
    ContentDto toDto(ContentInfoEntity entity);
}
