package com.povobolapo.organizer.dao.api;

import com.povobolapo.organizer.model.DictTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<DictTaskStatus, Integer> {
}
