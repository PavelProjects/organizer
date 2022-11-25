package com.povobolapo.organizer.controller.model.user;

import com.povobolapo.organizer.controller.model.SearchRequest;
import lombok.Data;

@Data
public class UserRelationSearch extends SearchRequest {
    private String login;
    private String relationType;
}
