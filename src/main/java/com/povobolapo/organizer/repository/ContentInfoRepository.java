package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.ContentInfoEntity;
import com.povobolapo.organizer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentInfoRepository extends JpaRepository<ContentInfoEntity, String> {
    List<ContentInfoEntity> findByContentId(String contentId);
    List<ContentInfoEntity> findByOwner(UserEntity owner);
}
