package com.biteplate.pattern.observer;

import com.biteplate.domain.notification.Notification;
import com.biteplate.repository.NotificationRepository;
import org.springframework.stereotype.Component;

@Component
public class ManagerDashboardObserver implements SystemObserver {
    private final NotificationRepository notificationRepository;

    public ManagerDashboardObserver(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void update(String eventType, String message) {
        if (eventType.toUpperCase().contains("ALLERGY") || eventType.toUpperCase().contains("RESERVATION")) {
            notificationRepository.save(new Notification("MANAGER", eventType, message));
        }
    }
}
