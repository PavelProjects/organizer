package com.povobolapo.organizer.controller.models;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class ObjectIdMapper {

    public String objectIdToString(int objectId) {
        return String.valueOf(objectId);
    }

    public int stringToObjectId(String string) {
        return Integer.parseInt(string);
    }

}