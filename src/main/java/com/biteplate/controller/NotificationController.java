package com.biteplate.controller;

import com.biteplate.domain.notification.Notification;
import com.biteplate.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) { this.notificationService = notificationService; }

    @GetMapping
    public List<Notification> recent() { return notificationService.recent(); }
}
