package com.biteplate.service;

import com.biteplate.domain.staff.*;
import com.biteplate.dto.LoginRequest;
import com.biteplate.dto.LoginResponse;
import com.biteplate.dto.RegisterStaffRequest;
import com.biteplate.repository.StaffRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final StaffRepository staffRepository;
    private final AuditService auditService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(StaffRepository staffRepository, AuditService auditService, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.auditService = auditService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        Staff staff = staffRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!passwordEncoder.matches(request.password(), staff.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        auditService.record("LOGIN", staff.getStaffId(), staff.getName() + " logged in as " + staff.getRole(), staff.getStaffId());
        return new LoginResponse(staff.getStaffId(), staff.getName(), staff.getRole().name(), "Login successful");
    }

    public Staff register(RegisterStaffRequest request) {
        StaffRole role;
        try {
            role = StaffRole.valueOf(request.role().toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid role. Use WAITER, HEAD_CHEF, CASHIER, or MANAGER.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Staff staff = switch (role) {
            case WAITER -> new Waiter(request.staffId(), request.username(), encodedPassword, request.name());
            case HEAD_CHEF -> new HeadChef(request.staffId(), request.username(), encodedPassword, request.name());
            case CASHIER -> new Cashier(request.staffId(), request.username(), encodedPassword, request.name());
            case MANAGER -> new Manager(request.staffId(), request.username(), encodedPassword, request.name());
        };

        Staff saved = staffRepository.save(staff);
        auditService.record("STAFF_REGISTERED", request.staffId(), "Staff registered: " + request.name() + " as " + role, request.staffId());
        return saved;
    }
}
