package com.povobolapo.organizer.dao.repository;

import com.povobolapo.organizer.model.DictTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<DictTaskStatus, Integer> {
    DictTaskStatus findByName(String name);
}
