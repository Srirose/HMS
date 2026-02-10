package com.zaalima.hospital.metrics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BedOccupancyMetrics {

    private int totalBeds;
    private int occupiedBeds;
    private List<WardOccupancy> perWard;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class WardOccupancy {
        private String ward;
        private int total;
        private int occupied;
    }
}


