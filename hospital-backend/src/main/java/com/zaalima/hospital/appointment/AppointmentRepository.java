package com.zaalima.hospital.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM Appointment a
            WHERE a.doctorId = :doctorId
              AND a.status IN ('CONFIRMED', 'PENDING')
              AND (a.startTime < :endTime AND a.endTime > :startTime)
            """)
    boolean existsOverlapping(@Param("doctorId") Long doctorId,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);

    List<Appointment> findByDoctorIdAndStartTimeBetween(Long doctorId,
                                                        LocalDateTime start,
                                                        LocalDateTime end);
}


