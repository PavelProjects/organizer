package com.povobolapo.organizer.service;

import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.model.DictNotifyType;
import com.povobolapo.organizer.model.NotificationEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.repository.NotificationRepository;
import com.povobolapo.organizer.repository.NotifyTypeRepository;
import com.povobolapo.organizer.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotifyTypeRepository notifyTypeRepository;
    private final UserService userService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               NotifyTypeRepository notifyTypeRepository,
                               UserService userService) {
        this.notificationRepository = notificationRepository;
        this.notifyTypeRepository = notifyTypeRepository;
        this.userService = userService;
    }

    public List<NotificationEntity> getUserNotifications(String login) {
        return notificationRepository.findByUserLogin(login);
    }

    // Метода для создания системных уведмолений
    // Например - обновите приложение, потеряна связь и т.п.
    @Transactional
    public void createSystemNotification(String toUser, String body) {
        log.debug("Creating new system notification for user {}", toUser);
        DictNotifyType notifyType = notifyTypeRepository.findByName(NotifyTypes.SYSTEM.getName());
        Objects.requireNonNull(notifyType);

        UserEntity userEntity = userService.getUserByLogin(toUser);
        if (userEntity == null) {
            throw new NotFoundException("Can't find user with login " + toUser);
        }

        NotificationEntity notificationEntity = new NotificationEntity(userEntity, body, notifyType);
        notificationRepository.save(notificationEntity);
        log.debug("System notification created");
    }

    @Transactional
    public void markNotificationsChecked(List<Integer> notificationIds) {
        log.debug("Marking notifications checked {}", notificationIds);
        List<NotificationEntity> notificationEntities = notificationRepository.findAllById(notificationIds);
        if (notificationEntities.isEmpty()) {
            log.warn("No notification were found");
            return;
        }

        notificationEntities.forEach(notificationEntity -> notificationEntity.setChecked(true));
        notificationRepository.saveAll(notificationEntities);
        log.debug("Notifications marked checked");
    }

    @Transactional
    public void deleteNotifications(List<Integer> notificationIds) throws AuthenticationException {
        log.debug("Deleting notifications {}", notificationIds);
        List<NotificationEntity> notificationEntities = notificationRepository.findAllById(notificationIds);
        if (notificationEntities.isEmpty()) {
            log.debug("No notification were found");
            return;
        }

        String currentUser = userService.authenticatedUserName();
        List<NotificationEntity> toDelete = notificationEntities.stream()
                .filter(notificationEntity -> StringUtils.equals(notificationEntity.getUser().getLogin(), currentUser)).collect(Collectors.toList());
        if (toDelete.isEmpty()) {
            log.debug("No notification to delete for user " + currentUser);
            return;
        }

        notificationRepository.deleteAll(toDelete);
        log.debug("Notifications deleted");
    }

    enum NotifyTypes {
        SYSTEM("system"),
        COMMENT("comment"),
        TASK("task");

        private final String name;

        NotifyTypes(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }
}
