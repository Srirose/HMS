package com.zaalima.hospital.patient;

import com.zaalima.hospital.admission.PatientAdmission;
import com.zaalima.hospital.admission.PatientAdmissionRepository;
import com.zaalima.hospital.patient.dto.PatientAdmissionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientAdmissionRepository admissionRepository;

    public PatientService(PatientRepository patientRepository, PatientAdmissionRepository admissionRepository) {
        this.patientRepository = patientRepository;
        this.admissionRepository = admissionRepository;
    }

    @Transactional
    public Patient admitPatient(PatientAdmissionDto admissionDto) {
        Patient patient = new Patient();
        patient.setFirstName(admissionDto.getFirstName());
        patient.setLastName(admissionDto.getLastName());
        patient.setDob(admissionDto.getDob());
        patient.setGender(admissionDto.getGender());
        patient.setContactNumber(admissionDto.getContactNumber());
        patient.setEmail(admissionDto.getEmail());
        patient.setAssignedDoctorUsername(admissionDto.getAssignedDoctorUsername());
        patient.setMedicalHistory(admissionDto.getMedicalHistory());
        patient.setAdmissionDetails(admissionDto.getAdmissionDetails());

        Patient savedPatient = patientRepository.save(patient);

        PatientAdmission admission = new PatientAdmission();
        admission.setPatientId(savedPatient.getId());
        admission.setAdmissionDate(LocalDate.now());
        admission.setMedicalHistory(admissionDto.getMedicalHistory());
        admission.setWard(admissionDto.getAdmissionDetails() != null ? (String) admissionDto.getAdmissionDetails().get("roomType") : null);
        admission.setBedNumber(admissionDto.getAdmissionDetails() != null ? (String) admissionDto.getAdmissionDetails().get("bedNumber") : null);
        admission.setAdmissionReason(admissionDto.getAdmissionDetails() != null ? (String) admissionDto.getAdmissionDetails().get("reasonForAdmission") : null);

        admissionRepository.save(admission);

        return savedPatient;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }
}