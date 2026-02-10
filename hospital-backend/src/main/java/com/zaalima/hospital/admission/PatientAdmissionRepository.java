package com.zaalima.hospital.admission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientAdmissionRepository extends JpaRepository<PatientAdmission, Long> {
}


