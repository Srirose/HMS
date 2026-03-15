package com.zaalima.hospital.controller;

import com.zaalima.hospital.User;
import com.zaalima.hospital.UserRepository;
import com.zaalima.hospital.audit.LogAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/doctors")
    @LogAccess(entity = "Doctor", action = "LIST")
    public List<User> getAllDoctors() {
        return userRepository.findByRole("DOCTOR");
    }

    @GetMapping("/nurses")
    @LogAccess(entity = "Nurse", action = "LIST")
    public List<User> getAllNurses() {
        return userRepository.findByRole("NURSE");
    }

    @GetMapping("/all-users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/users/{id}")
    @LogAccess(entity = "User", action = "DELETE")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}