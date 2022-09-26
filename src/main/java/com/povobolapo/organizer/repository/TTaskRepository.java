package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TTaskRepository extends JpaRepository<TaskEntity, Integer> {
}