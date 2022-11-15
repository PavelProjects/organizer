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
            @Mapping(target = "fileExtension", source = "content.fileExtension")
    })
    ContentDto toDto(ContentInfoEntity entity);
}
