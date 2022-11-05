package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    List<CommentEntity> findByTaskId(String taskId);
}
