package com.zaalima.hospital.patient;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private LocalDate dob;     // maps to column "dob" (OK)
    private String gender;     // maps to column "gender" (OK)

    @Column(name = "contact_number")
    private String contactNumber;

    private String email;      // maps to column "email" (OK)

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "admission_details", columnDefinition = "jsonb")
    private Map<String, Object> admissionDetails;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "medical_history", columnDefinition = "jsonb")
    private Map<String, Object> medicalHistory;

    @Column(name = "assigned_doctor_username")
    private String assignedDoctorUsername;


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Map<String, Object> getAdmissionDetails() { return admissionDetails; }
    public void setAdmissionDetails(Map<String, Object> admissionDetails) { this.admissionDetails = admissionDetails; }
    public Map<String, Object> getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(Map<String, Object> medicalHistory) { this.medicalHistory = medicalHistory; }
    public void setAssignedDoctorUsername(String assignedDoctorUsername2) {
        
        this.assignedDoctorUsername = assignedDoctorUsername2;
    }
    public String getAssignedDoctorUsername() {
        return assignedDoctorUsername;
    }
}