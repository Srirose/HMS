package com.zaalima.hospital.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("legacyDoctorController")
@RequestMapping("/doctor")
public class DoctorController1 {

    @GetMapping("/patients")
    public String doctorPatients() {
        return "Doctor access only";
    }
}
