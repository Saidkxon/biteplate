package com.biteplate.domain.audit;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actionType;
    private String referenceId;

    @Column(length = 1200)
    private String details;

    private String staffId;
    private LocalDateTime createdAt;

    protected AuditLog() {
    }

    public AuditLog(String actionType, String referenceId, String details, String staffId) {
        this.actionType = actionType;
        this.referenceId = referenceId;
        this.details = details;
        this.staffId = staffId;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getActionType() { return actionType; }
    public String getReferenceId() { return referenceId; }
    public String getDetails() { return details; }
    public String getStaffId() { return staffId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
