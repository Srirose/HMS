package com.zaalima.hospital.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zaalima.hospital.User;
import com.zaalima.hospital.UserService;
import com.zaalima.hospital.dto.LoginRequest;
import com.zaalima.hospital.dto.LoginResponse;
import com.zaalima.hospital.dto.RegisterRequest;
import com.zaalima.hospital.security.JwtUtil;
import com.zaalima.hospital.tenant.TenantContext;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestHeader("X-Tenant-ID") String tenant,
            @RequestBody RegisterRequest req) {

        try {
            TenantContext.setTenant(tenant);

            userService.createUser(
                    req.getUsername(),
                    req.getPassword(),
                    req.getRole()   
            );

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } finally {
            TenantContext.clear(); 
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestHeader("X-Tenant-ID") String tenant,
            @RequestBody LoginRequest request) {

        try {
            TenantContext.setTenant(tenant);

            User user = userService.authenticate(
                    request.getUsername(),
                    request.getPassword()
            );

            String token = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    new LoginResponse(token, user.getRole())
            );
        } finally {
            TenantContext.clear(); 
        }
    }
}
