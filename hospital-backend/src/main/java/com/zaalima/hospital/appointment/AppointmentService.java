package com.zaalima.hospital.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Appointment schedule(Appointment appointment) {
        boolean conflict = appointmentRepository.existsOverlapping(
                appointment.getDoctorId(),
                appointment.getStartTime(),
                appointment.getEndTime()
        );
        if (conflict) {
            throw new IllegalStateException("Doctor is already booked for this time slot");
        }
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findForDoctorWithinRange(Long doctorId,
                                                      LocalDateTime start,
                                                      LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndStartTimeBetween(doctorId, start, end);
    }
}


