package com.biteplate.repository;

import com.biteplate.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByReadFalse();
    List<Notification> findTop20ByOrderByCreatedAtDesc();
}
