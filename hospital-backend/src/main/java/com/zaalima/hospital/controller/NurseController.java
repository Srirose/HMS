package com.zaalima.hospital.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nurse")
@CrossOrigin(origins = "http://localhost:4200")
public class NurseController {

    @GetMapping("/ward")
    public String nurseWard() {
        return "Nurse access only";
    }
}
