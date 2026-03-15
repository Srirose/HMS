package com.zaalima.hospital.appointment;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import com.zaalima.hospital.patient.PatientRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentNotificationService notificationService;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              AppointmentNotificationService notificationService,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public Appointment schedule(Appointment appointment) {
        validateNoConflict(appointment);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment saved = appointmentRepository.save(appointment);
        
        // Trigger email notification
        patientRepository.findById(saved.getPatientId()).ifPresent(patient -> {
            if (patient.getEmail() != null) {
                notificationService.sendConfirmationEmail(patient.getEmail(), saved);
            }
        });
        
        return saved;
    }

    @Transactional
    public Appointment update(Appointment appointment) {
        validateNoConflict(appointment);
        return appointmentRepository.save(appointment);
    }

    private void validateNoConflict(Appointment appointment) {
        boolean conflict = appointmentRepository.existsOverlapping(
                appointment.getDoctorId(),
                appointment.getStartTime(),
                appointment.getEndTime()
        );
        // If it's an update, we need to ignore the current appointment record
        if (conflict && appointment.getId() != null) {
            List<Appointment> overlaps = appointmentRepository.findByDoctorIdAndStartTimeBetween(
                appointment.getDoctorId(), appointment.getStartTime(), appointment.getEndTime());
            conflict = overlaps.stream().anyMatch(a -> !a.getId().equals(appointment.getId()));
        }
        
        if (conflict) {
            throw new IllegalStateException("Doctor is already booked for this time slot");
        }
    }

    public List<Appointment> findForDoctorWithinRange(Long doctorId,
                                                      LocalDateTime start,
                                                      LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndStartTimeBetween(doctorId, start, end);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}


