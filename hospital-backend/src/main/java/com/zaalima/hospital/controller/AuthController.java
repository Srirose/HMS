package com.zaalima.hospital.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public AuthController(UserService userService) {
        this.userService = userService;
    }

   @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestHeader("X-Tenant-ID") String tenant,
            @RequestBody RegisterRequest req) {
        try{
        System.out.println("Register request for tenant = " + tenant);

        TenantContext.setTenant(tenant);

        userService.createUser(
                req.getUsername(),
                req.getPassword(),
                "ADMIN"
        );

        return ResponseEntity.ok("User registered");}
        catch(Exception e){
                System.out.println(e);
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,@RequestHeader("X-Tenant-ID") String tenant) {
        TenantContext.setTenant(tenant);

        User user = userService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        String role = user.getRole() != null ? String.valueOf(user.getRole()) : null;
        String token = JwtUtil.generateToken(
                user.getUsername(),
                role
        );

        return ResponseEntity.ok(
                new LoginResponse(token, role)
        );
    }

}
