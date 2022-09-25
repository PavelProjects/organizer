package com.povobolapo.organizer.dao.repository;

import com.povobolapo.organizer.model.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<TUser, Integer> {
    TUser findByLogin(String login);
}
