package com.zaalima.hospital.appointment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "appointment",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"doctor_id", "start_time"})
        }
)
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppointmentStatus status = AppointmentStatus.CONFIRMED;

    @Column(length = 255)
    private String notes;
}


