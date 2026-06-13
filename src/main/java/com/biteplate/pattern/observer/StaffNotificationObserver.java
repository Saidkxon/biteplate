package com.biteplate.pattern.observer;

import com.biteplate.domain.notification.Notification;
import com.biteplate.repository.NotificationRepository;
import org.springframework.stereotype.Component;

@Component
public class StaffNotificationObserver implements SystemObserver {
    private final NotificationRepository notificationRepository;

    public StaffNotificationObserver(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void update(String eventType, String message) {
        notificationRepository.save(new Notification("STAFF", eventType, message));
    }
}
