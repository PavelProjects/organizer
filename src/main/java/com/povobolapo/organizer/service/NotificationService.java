package com.povobolapo.organizer.service;

import com.povobolapo.organizer.mapper.UserMapper;
import com.povobolapo.organizer.model.*;
import com.povobolapo.organizer.repository.NotificationRepository;
import com.povobolapo.organizer.repository.NotifyTypeRepository;
import com.povobolapo.organizer.utils.EventDispatcher;
import com.povobolapo.organizer.utils.TemplateConverter;
import com.povobolapo.organizer.websocket.model.NotificationMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
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
    private final EventDispatcher eventDispatcher;

    private final UserMapper userMapper;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               NotifyTypeRepository notifyTypeRepository,
                               EventDispatcher eventDispatcher,
                               UserMapper userMapper){
        this.notificationRepository = notificationRepository;
        this.notifyTypeRepository = notifyTypeRepository;
        this.eventDispatcher = eventDispatcher;
        this.userMapper = userMapper;
    }

    public List<NotificationEntity> getUserNotifications() throws AuthenticationException {
        return notificationRepository.findByUserLogin(UserAuthoritiesService.getCurrentUserLogin());
    }

    // Метод создания уведомлений для комментариев

    @Transactional
    public void createCommentNotification(Collection<UserEntity> users, CommentEntity comment) {
        users.forEach(u -> createCommentNotification(u, comment));
    }

    public void createCommentNotification(UserEntity userEntity, CommentEntity comment) {
        createNotification(userEntity, comment.getAuthor(), comment.getBody(), NotifyTypes.COMMENT);
    }

    @Transactional
    public void createTaskNotification(TaskEntity task, @NotNull @NotEmpty String bodyTemplate) {
        task.getParticipants().forEach(u -> createTaskNotification(u, task, TemplateConverter.convertTemplate(bodyTemplate, u, task)));
    }

    // Метод создания уведомлений для тасок
    // Например таска создана, таска обновлена
    public void createTaskNotification(UserEntity userEntity, TaskEntity task, @NotNull @NotEmpty String bodyTemplate) {
        createNotification(userEntity, task.getAuthor(), bodyTemplate, NotifyTypes.TASK);
    }

    @Transactional
    public void createNotification(@NotNull UserEntity toUser, UserEntity creator, @NotNull String body, @NotNull NotifyTypes notifyType) {
        checkNotificationCount(toUser.getLogin());
        log.debug("Creating new notification for user {} type {}", toUser, notifyType.name());
        NotificationEntity notificationEntity = createBasicNotification(toUser, NotifyTypes.TASK.getName());
        notificationEntity.setBody(body);
        if (creator != null) {
            notificationEntity.setCreator(creator);
        }
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
                userMapper.toDto(notificationEntity.getUser()),
                userMapper.toDto(notificationEntity.getCreator()),
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
        List<NotificationEntity> notificationEntities = notificationRepository.findByIdInAndUserLogin(notificationIds, UserAuthoritiesService.getCurrentUserLogin());
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
        List<NotificationEntity> notificationEntities = notificationRepository.findByIdInAndUserLogin(notificationIds, UserAuthoritiesService.getCurrentUserLogin());
        if (notificationEntities.isEmpty()) {
            log.debug("No notification were found");
            return;
        }
        deleteNotifications(notificationEntities);
    }

    @Transactional
    public void deleteNotifications(List<NotificationEntity> notificationEntities) throws AuthenticationException{
        String currentUser = UserAuthoritiesService.getCurrentUserLogin();
        List<NotificationEntity> toDelete = notificationEntities.stream()
                .filter(notificationEntity -> StringUtils.equals(notificationEntity.getUser().getLogin(), currentUser)).collect(Collectors.toList());
        if (toDelete.isEmpty()) {
            log.debug("No notification to delete for user " + currentUser);
            return;
        }

        notificationRepository.deleteAll(toDelete);
        log.debug("Notifications deleted");
    }

    private NotificationEntity createBasicNotification(UserEntity userEntity, String type) {
        DictNotifyType notifyType = notifyTypeRepository.findByName(type);
        Objects.requireNonNull(notifyType);
        return new NotificationEntity(userEntity, notifyType);
    }

    public enum NotifyTypes {
        SYSTEM("system"),
        COMMENT("comment"),
        TASK("task"),
        USER("user");

        private final String name;

        NotifyTypes(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }
}
