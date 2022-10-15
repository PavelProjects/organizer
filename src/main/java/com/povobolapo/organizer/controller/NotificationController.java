package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.NotificationRequest;
import com.povobolapo.organizer.controller.model.NotificationResponse;
import com.povobolapo.organizer.model.NotificationEntity;
import com.povobolapo.organizer.service.NotificationService;
import com.povobolapo.organizer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> getUserNotifications() throws AuthenticationException {
        log.info("GET-request: getUserNotifications(})");
        List<NotificationEntity> notifications = notificationService.getUserNotifications();
        return notifications.stream().map(NotificationResponse::new).collect(Collectors.toList());
    }

    @PutMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public void setChecked(@RequestBody NotificationRequest notificationRequest) throws AuthenticationException {
        log.info("PUT-request: setChecked(request={})", notificationRequest);
        notificationService.markNotificationsChecked(notificationRequest.getIds());
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNotification(@RequestBody NotificationRequest notificationRequest) throws AuthenticationException {
        log.info("DELETE-request: deleteNotification(request={})", notificationRequest);
        notificationService.deleteNotificationsByIds(notificationRequest.getIds());
    }
}
