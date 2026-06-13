package com.biteplate.service;

import com.biteplate.domain.audit.AuditLog;
import com.biteplate.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {
    private final AuditLogRepository auditRepository;

    public AuditService(AuditLogRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void record(String actionType, String referenceId, String details, String staffId) {
        auditRepository.save(new AuditLog(actionType, referenceId, details, staffId));
    }

    public List<AuditLog> recent() {
        return auditRepository.findTop50ByOrderByCreatedAtDesc();
    }
}
