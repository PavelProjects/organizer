package com.povobolapo.organizer.service;

import com.povobolapo.organizer.controller.model.CommentDto;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.exception.ValidationException;
import com.povobolapo.organizer.model.CommentEntity;
import com.povobolapo.organizer.model.TaskEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.repository.CommentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Scope("singleton")
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final NotificationService notificationService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService,
                          TaskService taskService, NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.taskService = taskService;
        this.notificationService = notificationService;
    }

    @Transactional
    public CommentEntity createComment(String taskId, String body) throws AuthenticationException {
        UserEntity currentUser = Objects.requireNonNull(userService.getCurrentUser(), "Can't find current user");
        TaskEntity task = Objects.requireNonNull(taskService.getTaskById(taskId), "Can't find by id " + taskId);

        CommentEntity comment = new CommentEntity(currentUser, task, body);
        comment.setCreationDate(new Date());
        commentRepository.save(comment);
        createNotification(comment);
        return comment;
    }

    private void createNotification(CommentEntity comment) {
        // TODO рассылка уведомления участникам задачи
        // реализовать после реализации исполнителей задачи
        // Тестовая реализация
        notificationService.createCommentNotification(comment.getAuthor(), comment);
    }

    public List<CommentEntity> getTaskComments(String taskId) {
        return commentRepository.findByTaskId(taskId);
    }
    public CommentEntity getComment(String commentId) {
        return commentRepository.findById(commentId).get();
    }

    @Transactional
    public void editComment(CommentDto commentDto) throws AuthenticationException, AccessDeniedException {
        if (StringUtils.isBlank(commentDto.getId())) {
            throw new ValidationException("Comment id is missing");
        }
        Optional<CommentEntity> comment = commentRepository.findById(commentDto.getId());
        if (comment.isEmpty()) {
            throw new NotFoundException("Can't found comment by id " + commentDto.getId());
        }
        // Только автор может редактировать свой комментарий
        if (!canManageComment(comment.get())) {
            throw new AccessDeniedException("You can't edit this comment!");
        }

        comment.get().setBody(commentDto.getBody());
        commentRepository.save(comment.get());
    }

    @Transactional
    public void deleteComment(String commentId) throws AuthenticationException, AccessDeniedException {
        if (StringUtils.isBlank(commentId)) {
            throw new ValidationException("Comment id is missing");
        }
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new NotFoundException("Can't found comment by id " + commentId);
        }
        // Только автор может редактировать свой комментарий
        if (!canManageComment(comment.get())) {
            throw new AccessDeniedException("You can't delete this comment!");
        }
        commentRepository.delete(comment.get());
    }

// TODO сделать проверку на получение комментов изи задачи. Проверять на вхождение в участники
    private boolean canManageComment(CommentEntity comment) throws AuthenticationException {
        Objects.requireNonNull(comment);
        UserEntity currentUser = userService.getCurrentUser();
        return StringUtils.equals(currentUser.getLogin(), comment.getAuthor().getLogin());
    }
}
