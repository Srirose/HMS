package com.zaalima.hospital.appointment;

import java.time.LocalDateTime;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.zaalima.hospital.patient.PatientRepository;
import java.util.List;

import java.util.Optional;

@Service
public class AppointmentNotificationService {

    private final JavaMailSender mailSender;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentNotificationService(Optional<JavaMailSender> mailSender, 
                                        AppointmentRepository appointmentRepository,
                                        PatientRepository patientRepository) {
        this.mailSender = mailSender.orElse(null);
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public void sendConfirmationEmail(String to, Appointment appt) {
        if (mailSender == null) {
            System.out.println("Mail sender not configured. Skipping email to: " + to);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Appointment Confirmed");
        message.setText("Your appointment is confirmed on " + appt.getStartTime());
        mailSender.send(message);
    }

    public void sendReminderEmail(String to, Appointment appt) {
        if (mailSender == null) {
            System.out.println("Mail sender not configured. Skipping reminder to: " + to);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Appointment Reminder");
        message.setText("Reminder: you have an appointment at " + appt.getStartTime());
        mailSender.send(message);
    }

    @Scheduled(fixedDelay = 300000)
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inOneHour = now.plusHours(1);
        
        List<Appointment> upcoming = appointmentRepository.findByStartTimeBetween(now, inOneHour);
        for (Appointment appt : upcoming) {
            patientRepository.findById(appt.getPatientId()).ifPresent(patient -> {
                if (patient.getEmail() != null) {
                    sendReminderEmail(patient.getEmail(), appt);
                }
            });
        }
    }
}
