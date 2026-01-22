package com.zaalima.hospital.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.http.ResponseEntity;
// import com.zaalima.hospital.UserService;
//import com.zaalima.hospital.CreateUserRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Admin access only";
    }

    // @PostMapping("/create-user")
    // public ResponseEntity<String> createUser(@RequestBody UserService req) {
    //     UserService.createUser(
    //             req.getUsername(),
    //             req.getPassword(),
    //             req.getRole()
    //     );
    //     return ResponseEntity.ok("User created");
    // }
}