package com.povobolapo.organizer.dao.api;

import com.povobolapo.organizer.model.TTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TTaskRepository extends JpaRepository<TTask, Integer> {
}
