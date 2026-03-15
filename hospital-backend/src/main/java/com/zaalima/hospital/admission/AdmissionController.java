package com.zaalima.hospital.admission;

import com.zaalima.hospital.audit.LogAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdmissionController {

    private final PatientAdmissionRepository admissionRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LogAccess(entity = "PatientAdmission", action = "CREATE")
    public PatientAdmission create(@RequestBody PatientAdmission admission) {
        return admissionRepository.save(admission);
    }

    @GetMapping("/{id}")
    @LogAccess(entity = "PatientAdmission", action = "VIEW")
    public ResponseEntity<PatientAdmission> get(@PathVariable Long id) {
        return admissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PatientAdmission> list() {
        return admissionRepository.findAll();
    }
}


