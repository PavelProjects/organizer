package com.povobolapo.organizer.service;

import com.povobolapo.organizer.websocket.model.NotificationMessage;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.model.*;
import com.povobolapo.organizer.repository.NotificationRepository;
import com.povobolapo.organizer.repository.NotifyTypeRepository;
import com.povobolapo.organizer.utils.EventDispatcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class NotificationService {
    private static final int MAX_USER_NOTIFICATIONS = 100;
    // TODO WebSocket для получения уведомлений; Тесты в insomnia
    // Вынести типы уведомлений в кеши

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotifyTypeRepository notifyTypeRepository;
    private final UserService userService;
    private final EventDispatcher eventDispatcher;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               NotifyTypeRepository notifyTypeRepository,
                               UserService userService,
                               EventDispatcher eventDispatcher){
        this.notificationRepository = notificationRepository;
        this.notifyTypeRepository = notifyTypeRepository;
        this.userService = userService;
        this.eventDispatcher = eventDispatcher;
    }

    public List<NotificationEntity> getUserNotifications() throws AuthenticationException {
        return notificationRepository.findByUserLogin(userService.authenticatedUserName());
    }

    // Метод для создания системных уведмолений
    // Например - обновите приложение, потеряна связь и т.п.
    @Transactional
    public void createSystemNotification(String toUser, String body) {
        checkNotificationCount(toUser);
        log.debug("Creating new system notification for user {}", toUser);
        NotificationEntity notificationEntity = createBasicNotification(toUser, NotifyTypes.SYSTEM.getName());
        notificationEntity.setBody(body);
        notificationRepository.save(notificationEntity);
        log.debug("System notification created");

        try {
            dispatchNotification(notificationEntity);
        } catch (Exception ex) {
            log.error("Failed to dispatch notification", ex);
        }
    }

    // Метод создания уведомлений для комментариев
    @Transactional
    public void createCommentNotification(String toUser, CommentEntity comment) {
        checkNotificationCount(toUser);
        log.debug("Creating new comment notification for user {} for comment {}", toUser, comment);
        NotificationEntity notificationEntity = createBasicNotification(toUser, NotifyTypes.COMMENT.getName());
        notificationEntity.setBody(comment.getBody());
        notificationEntity.setCreator(comment.getAuthor());
        notificationRepository.save(notificationEntity);
        log.debug("Comment notification created");

        try {
            dispatchNotification(notificationEntity);
        } catch (Exception ex) {
            log.error("Failed to dispatch notification", ex);
        }
    }

    // Метод создания уведомлений для тасок
    // Например таска создана, таска обновлена
    @Transactional
    public void createTaskNotification(String toUser, TaskEntity task) {
        checkNotificationCount(toUser);
        log.debug("Creating new task notification for user {} for task {}", toUser, task);
        NotificationEntity notificationEntity = createBasicNotification(toUser, NotifyTypes.TASK.getName());
        notificationEntity.setBody(getTaskNotificationBody(task));
        notificationEntity.setCreator(task.getAuthor());
        notificationRepository.save(notificationEntity);
        log.debug("Task notification created");

        try {
            dispatchNotification(notificationEntity);
        } catch (Exception ex) {
            log.error("Failed to dispatch notification", ex);
        }
    }

    // Рассылка системного события
    // Регистрация хендлеров в RuntimeConfig
    private void dispatchNotification(NotificationEntity notificationEntity) throws Exception {
        eventDispatcher.dispatch(new NotificationMessage(
                notificationEntity.getUser(),
                notificationEntity.getCreator(),
                notificationEntity.getType().getCaption(),
                notificationEntity.getBody()));
    }

    private void checkNotificationCount(String login) {
        log.debug("Checking amount of user[{}] notifications", login);
        int total = notificationRepository.countByUserLogin(login);
        if (total > MAX_USER_NOTIFICATIONS) {
            List<NotificationEntity> oldest = notificationRepository.findOldestUserNotifications(login, MAX_USER_NOTIFICATIONS - total);
            Objects.requireNonNull(oldest);
            log.debug("Deleting {} old notifications for user [{}]", oldest.size(), login);
            try {
                deleteNotifications(oldest);
            } catch (AuthenticationException exc) {
                throw new RuntimeException(exc);
            }
        }
    }

    @Transactional
    public void markNotificationsChecked(List<String> notificationIds) throws AuthenticationException {
        log.debug("Marking notifications checked {}", notificationIds);
        List<NotificationEntity> notificationEntities = notificationRepository.findByIdInAndUserLogin(notificationIds, userService.authenticatedUserName());
        if (notificationEntities.isEmpty()) {
            log.warn("No notification were found");
            return;
        }

        notificationEntities.forEach(notificationEntity -> notificationEntity.setChecked(true));
        notificationRepository.saveAll(notificationEntities);
        log.debug("Notifications marked checked");
    }

    @Transactional
    public void deleteNotificationsByIds(List<String> notificationIds) throws AuthenticationException {
        log.debug("Deleting notifications {}", notificationIds);
        List<NotificationEntity> notificationEntities = notificationRepository.findByIdInAndUserLogin(notificationIds, userService.authenticatedUserName());
        if (notificationEntities.isEmpty()) {
            log.debug("No notification were found");
            return;
        }
        deleteNotifications(notificationEntities);
    }

    @Transactional
    public void deleteNotifications(List<NotificationEntity> notificationEntities) throws AuthenticationException{
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

    private String getTaskNotificationBody(TaskEntity task) {
        return String.format("Задача [%s] - [%s] была изменена",
                task.getName(), task.getTaskStatus().getCaption());
    }

    private NotificationEntity createBasicNotification(String toUser, String type) {
        DictNotifyType notifyType = notifyTypeRepository.findByName(type);
        Objects.requireNonNull(notifyType);

        UserEntity userEntity = userService.getUserByLogin(toUser);
        if (userEntity == null) {
            throw new NotFoundException("Can't find user with login " + toUser);
        }

        return new NotificationEntity(userEntity, notifyType);
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
