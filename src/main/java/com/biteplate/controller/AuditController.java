package com.biteplate.controller;

import com.biteplate.domain.audit.AuditLog;
import com.biteplate.service.AuditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
    private final AuditService auditService;
    public AuditController(AuditService auditService) { this.auditService = auditService; }

    @GetMapping
    public List<AuditLog> recent() { return auditService.recent(); }
}
