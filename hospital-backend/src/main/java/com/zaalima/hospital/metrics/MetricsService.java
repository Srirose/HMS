package com.zaalima.hospital.metrics;

import com.zaalima.hospital.admission.PatientAdmissionRepository;
import com.zaalima.hospital.appointment.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final PatientAdmissionRepository admissionRepository;
    private final AppointmentRepository appointmentRepository;

    public BedOccupancyMetrics getBedOccupancy() {
        // In a real system, these would come from a 'Beds' table.
        // For this demo, we'll simulate based on admissions.
        long totalAdmissions = admissionRepository.count();
        
        // Simulating fixed capacity per ward
        return new BedOccupancyMetrics(
                200,
                (int) totalAdmissions,
                List.of(
                        new BedOccupancyMetrics.WardOccupancy("ICU", 20, Math.min(20, (int)(totalAdmissions * 0.1))),
                        new BedOccupancyMetrics.WardOccupancy("Surgery", 40, Math.min(40, (int)(totalAdmissions * 0.3))),
                        new BedOccupancyMetrics.WardOccupancy("General", 140, Math.min(140, (int)(totalAdmissions * 0.6)))
                )
        );
    }

    public Map<String, Long> getAdmissionTrends() {
        return List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").stream()
                .collect(Collectors.toMap(day -> day, day -> (long) (Math.random() * 20)));
    }

    public Map<Long, Long> getDoctorWorkload() {
        return appointmentRepository.findAll().stream()
                .collect(Collectors.groupingBy(appt -> appt.getDoctorId(), Collectors.counting()));
    }
}
