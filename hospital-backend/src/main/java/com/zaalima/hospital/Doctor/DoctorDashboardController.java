package com.zaalima.hospital.Doctor; // prefer lowercase package name

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import java.util.List;
import com.zaalima.hospital.patient.PatientRepository;
import com.zaalima.hospital.patient.Patient;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorDashboardController {

    private final PatientRepository patientRepository;

    public DoctorDashboardController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/admissions")
    public List<Patient> getMyAdmissions(Authentication auth) {
        String username = auth.getName(); // doctor's login username
        return patientRepository.findByAssignedDoctorUsername(username);
    }
}
