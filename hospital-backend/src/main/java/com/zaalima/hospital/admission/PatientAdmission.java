package com.zaalima.hospital.admission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "patient_admission")
@Getter
@Setter
public class PatientAdmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private LocalDate admissionDate;

    @Column(length = 50)
    private String ward;

    @Column(length = 20)
    private String bedNumber;

    @Column(length = 255)
    private String admissionReason;

    /**
     * Semi-structured medical history (FHIR-like) stored as JSONB.
     */
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> medicalHistory;

    @JsonIgnore
    public Object getFieldFromMedicalHistory(String key) {
        return medicalHistory != null ? medicalHistory.get(key) : null;
    }
}


