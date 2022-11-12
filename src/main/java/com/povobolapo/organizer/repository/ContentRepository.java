package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<ContentEntity, String> {
    ContentEntity findByHashCode(int hashCode);
}
