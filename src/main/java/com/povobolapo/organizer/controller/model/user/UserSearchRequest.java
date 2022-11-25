package com.povobolapo.organizer.controller.model.user;

import com.povobolapo.organizer.controller.model.SearchRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserSearchRequest extends SearchRequest implements Serializable {
    private String login;
    private String name;
}
