package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.CommentDto;
import com.povobolapo.organizer.controller.model.CreateCommentRequest;
import com.povobolapo.organizer.mapper.CommentMapper;
import com.povobolapo.organizer.model.CommentEntity;
import com.povobolapo.organizer.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @Operation(description = "Создание коммента")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody CreateCommentRequest commentRequest) throws AuthenticationException {
        Objects.requireNonNull(commentRequest);
        CommentEntity comment = commentService.createComment(commentRequest.getTaskId(), commentRequest.getBody());
        return commentMapper.toDto(comment);
    }

    @Operation(description = "Получение коммента")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getComment(@RequestParam String commentId) throws AuthenticationException, AccessDeniedException {
        CommentEntity comment = commentService.getComment(commentId);
        if (comment == null) {
            return null;
        }
        return commentMapper.toDto(comment);
    }

    @Operation(description = "Обновление коммента")
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(@RequestBody CommentDto commentDto) throws AuthenticationException, AccessDeniedException {
        Objects.requireNonNull(commentDto);
        commentService.editComment(commentDto);
    }

    @Operation(description = "Удаление коммента")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@RequestParam String commentId) throws AuthenticationException, AccessDeniedException {
        commentService.deleteComment(commentId);
    }


    @Operation(description = "Получение всех комментариев из задачи")
    @GetMapping(value = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getTaskComments(@RequestParam String taskId) {
        List<CommentEntity> comments = commentService.getTaskComments(taskId);
        if (comments == null || comments.isEmpty()) {
            return Collections.emptyList();
        }
        return comments.stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

}
