package com.biteplate.service;

import com.biteplate.domain.notification.Notification;
import com.biteplate.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> recent() {
        return notificationRepository.findTop20ByOrderByCreatedAtDesc();
    }
}
