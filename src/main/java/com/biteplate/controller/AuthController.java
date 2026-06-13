package com.biteplate.controller;

import com.biteplate.domain.staff.Staff;
import com.biteplate.dto.LoginRequest;
import com.biteplate.dto.LoginResponse;
import com.biteplate.dto.RegisterStaffRequest;
import com.biteplate.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public Staff register(@Valid @RequestBody RegisterStaffRequest request) {
        return authService.register(request);
    }
}
