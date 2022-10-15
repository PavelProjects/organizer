package com.povobolapo.organizer.repository;

import com.povobolapo.organizer.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findByUserLogin(String userLogin);
    List<NotificationEntity> findByIdInAndUserLogin(List<Integer> id, String userLogin);

    @Query("select count(n) from NotificationEntity n where user_login = ?1")
    int countByUserLogin(String userLogin);

    @Query(value = "select n from NotificationEntity n where user_login = ?1 order by creation_date asc limit ?2", nativeQuery = true)
    List<NotificationEntity> findOldestUserNotifications(String userLogin, int count);

}