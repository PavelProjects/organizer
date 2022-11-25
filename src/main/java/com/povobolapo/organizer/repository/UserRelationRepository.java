package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.UserRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRelationRepository extends JpaRepository<UserRelationEntity, String>, JpaSpecificationExecutor<UserRelationEntity> {
}
