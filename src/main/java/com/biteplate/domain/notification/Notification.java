package com.biteplate.domain.notification;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientRole;
    private String title;

    @Column(length = 1000)
    private String message;

    private boolean read;
    private LocalDateTime createdAt;

    protected Notification() {
    }

    public Notification(String recipientRole, String title, String message) {
        this.recipientRole = recipientRole;
        this.title = title;
        this.message = message;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getRecipientRole() { return recipientRole; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public boolean isRead() { return read; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void markRead() { this.read = true; }
}
