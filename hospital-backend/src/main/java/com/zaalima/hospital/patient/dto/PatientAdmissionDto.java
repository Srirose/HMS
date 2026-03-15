package com.zaalima.hospital.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Map;

public class PatientAdmissionDto {

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotBlank(message = "Contact number is mandatory")
    private String contactNumber;

    @Email(message = "Email should be valid")
    private String email;

    private Map<String, Object> admissionDetails;

    private Map<String, Object> medicalHistory;

    private Map<String, Object> allergies;

    private Map<String, Object> medications;

    private String assignedDoctorUsername;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getAdmissionDetails() {
        return admissionDetails;
    }

    public void setAdmissionDetails(Map<String, Object> admissionDetails) {
        this.admissionDetails = admissionDetails;
    }

    public Map<String, Object> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(Map<String, Object> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Map<String, Object> getAllergies() {
        return allergies;
    }

    public void setAllergies(Map<String, Object> allergies) {
        this.allergies = allergies;
    }

    public Map<String, Object> getMedications() {
        return medications;
    }

    public void setMedications(Map<String, Object> medications) {
        this.medications = medications;
    }

    public String getAssignedDoctorUsername() {
        return assignedDoctorUsername;
    }

    public void setAssignedDoctorUsername(String assignedDoctorUsername) {
        this.assignedDoctorUsername = assignedDoctorUsername;
    }
}
