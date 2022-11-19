package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> , JpaSpecificationExecutor<UserEntity> {
    UserEntity findByLogin(String login);
}
