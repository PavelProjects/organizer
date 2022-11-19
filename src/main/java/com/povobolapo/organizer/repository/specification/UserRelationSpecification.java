package com.povobolapo.organizer.repository.specification;

import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.model.UserRelationEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserRelationSpecification {
    public static Specification<UserRelationEntity> bothUsers(UserEntity user) {
        return (root, query, cb) -> cb.and(cb.equal(root.get("firstUser"), user), cb.equal(root.get("secondUser"), user));
    }

    public static Specification<UserRelationEntity> relationBetween(UserEntity firstUser, UserEntity secondUser) {
        return (root, query, cb) -> cb.or(
                cb.and(cb.equal(root.get("firstUser"), firstUser),cb.equal(root.get("secondUser"), secondUser)),
                cb.and(cb.equal(root.get("firstUser"), secondUser),cb.equal(root.get("secondUser"), firstUser))
        );
    }

    public static Specification<UserRelationEntity> relationType(UserRelationEntity.RelationType relationType) {
        return (root, query, cb) -> cb.equal(root.get("relationType"), relationType.name());
    }

    public static Specification<UserRelationEntity> relationBetween(UserEntity firstUser, UserEntity secondUser, UserRelationEntity.RelationType relationType) {
        Specification<UserRelationEntity> specification = Specification.where(null);
        specification.and(relationBetween(firstUser, secondUser));
        specification.and(relationType(relationType));
        return specification;
    }

    public static Specification<UserRelationEntity> userRelations(UserEntity user, UserRelationEntity.RelationType relationType) {
        Specification<UserRelationEntity> specification = Specification.where(null);
        specification.and(bothUsers(user));
        if (relationType != null) {
            specification.and(relationType(relationType));
        }
        return specification;
    }
}
