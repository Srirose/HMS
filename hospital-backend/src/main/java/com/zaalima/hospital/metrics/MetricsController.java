package com.zaalima.hospital.metrics;

import com.zaalima.hospital.audit.LogAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MetricsController {

    private final MetricsService metricsService;

    // In a real system this would be calculated from bed & admission tables.
    @GetMapping("/bed-occupancy")
    @LogAccess(entity = "BedOccupancy", action = "VIEW")
    public BedOccupancyMetrics bedOccupancy() {
        return metricsService.getBedOccupancy();
    }

    @GetMapping("/admission-trends")
    @LogAccess(entity = "AdmissionTrends", action = "VIEW")
    public java.util.Map<String, Long> admissionTrends() {
        return metricsService.getAdmissionTrends();
    }

    @GetMapping("/doctor-workload")
    @LogAccess(entity = "DoctorWorkload", action = "VIEW")
    public java.util.Map<Long, Long> doctorWorkload() {
        return metricsService.getDoctorWorkload();
    }
}


