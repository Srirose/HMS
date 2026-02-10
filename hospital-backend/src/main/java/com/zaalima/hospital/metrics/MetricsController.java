package com.zaalima.hospital.metrics;

import com.zaalima.hospital.audit.LogAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    // In a real system this would be calculated from bed & admission tables.
    @GetMapping("/bed-occupancy")
    @LogAccess(entity = "BedOccupancy", action = "VIEW")
    public BedOccupancyMetrics bedOccupancy() {
        return new BedOccupancyMetrics(
                200,
                160,
                List.of(
                        new BedOccupancyMetrics.WardOccupancy("ICU", 20, 18),
                        new BedOccupancyMetrics.WardOccupancy("Surgery", 40, 30),
                        new BedOccupancyMetrics.WardOccupancy("General", 140, 112)
                )
        );
    }
}


