package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.UserCreditsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCreditsRepository extends JpaRepository<UserCreditsEntity, String> {
    UserCreditsEntity findByLogin(String login);
}
