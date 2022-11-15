package com.povobolapo.organizer.utils;

import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Set;

public class TaskSpecifications {

    public static Specification<TaskEntity> hasAuthor(String login) {
        return (root, query, cb) -> cb.equal(root.get("author").get("login"), login);
    }

    public static Specification<TaskEntity> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("dictTaskStatus").get("name"), status);
    }

    public static Specification<TaskEntity> hasParticipants(Set<String> logins) {
        return (root, query, cb) -> {
            query.distinct(true);
            SetJoin<TaskEntity, UserEntity> participants = root.joinSet("participants");
            return participants.get("login").in(logins);
        };
    }
}
