package com.povobolapo.organizer;

import com.povobolapo.organizer.controller.model.comment.CommentDto;
import com.povobolapo.organizer.controller.model.notification.NotificationDto;
import com.povobolapo.organizer.controller.model.task.TaskDto;
import com.povobolapo.organizer.controller.model.user.UserDto;
import com.povobolapo.organizer.mapper.CommentMapper;
import com.povobolapo.organizer.mapper.NotificationMapper;
import com.povobolapo.organizer.mapper.TaskMapper;
import com.povobolapo.organizer.mapper.UserMapper;
import com.povobolapo.organizer.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashSet;

@SpringBootTest(classes = OrganizerApplication.class)
public class MappersTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    private final UserEntity userEntity = new UserEntity("id", "name", "login", "avatar");
    private final TaskEntity task = new TaskEntity(
            "id",
            "name",
            "description",
            new DictTaskStatus("id", "name", "caption"),
            new Date(),
            new Date(),
            userEntity,
            new HashSet<>()
    );
    private final CommentEntity comment = new CommentEntity(
            "id",
            new Date(),
            userEntity,
            task,
            "body"
    );


    @Test
    public void user() {
        UserDto dto = userMapper.toDto(userEntity);
        assert dto.getLogin().equals(userEntity.getLogin());
        assert dto.getName().equals(userEntity.getName());
        assert dto.getAvatar().equals(userEntity.getAvatar());
    }

    @Test
    public void task() {
        TaskDto dto = taskMapper.toDto(task);
        assert dto.getId().equals(task.getId());
        assert dto.getName().equals(task.getName());
        assert dto.getAuthor().getLogin().equals(task.getAuthor().getLogin());
        assert dto.getDeadline().equals(task.getDeadline());
        assert dto.getStatus().equals(task.getDictTaskStatus().getCaption());

        TaskEntity taskEntity = taskMapper.toEntity(dto);
        assert taskEntity.getId().equals(task.getId());
        assert taskEntity.getName().equals(task.getName());
        assert taskEntity.getAuthor().getLogin().equals(task.getAuthor().getLogin());
        assert taskEntity.getDeadline().equals(task.getDeadline());
    }

    @Test
    public void comment() {
        CommentDto dto = commentMapper.toDto(comment);
        assert dto.getId().equals(comment.getId());
        assert dto.getBody().equals(comment.getBody());
        assert dto.getCreationDate().equals(comment.getCreationDate());
        assert dto.getTask().getId().equals(task.getId());
        assert dto.getAuthor().getLogin().equals(userEntity.getLogin());


        CommentEntity commentEntity = commentMapper.toEntity(dto);
        assert commentEntity.getId().equals(comment.getId());
        assert commentEntity.getBody().equals(comment.getBody());
        assert commentEntity.getCreationDate().equals(comment.getCreationDate());
        assert commentEntity.getTask().getId().equals(task.getId());
        assert commentEntity.getAuthor().getLogin().equals(userEntity.getLogin());
    }

    @Test
    public void notification() {
        NotificationEntity notificationEntity = new NotificationEntity(
                "id",
                new Date(),
                userEntity,
                task,
                comment,
                userEntity,
                "body",
                new DictNotifyType(),
                false
        );

        NotificationDto dto = notificationMapper.toDto(notificationEntity);
        assert  dto.getId().equals(notificationEntity.getId());
        assert  dto.getBody().equals(notificationEntity.getBody());
        assert  dto.getUser().getLogin().equals(notificationEntity.getUser().getLogin());
        assert  dto.getCreator().getLogin().equals(notificationEntity.getCreator().getLogin());
        assert  dto.getCreationDate().equals(notificationEntity.getCreationDate());
    }
}
