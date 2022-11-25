package com.povobolapo.organizer.repository.specification;

import com.povobolapo.organizer.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<UserEntity> fieldStartsWith(String field, String login) {
        return (root, query, cb) -> cb.like(root.get(field), login + "%");
    }
}
