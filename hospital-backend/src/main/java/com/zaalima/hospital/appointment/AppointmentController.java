package com.zaalima.hospital.appointment;

import com.zaalima.hospital.audit.LogAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LogAccess(entity = "Appointment", action = "CREATE")
    public Appointment schedule(@RequestBody Appointment appointment) {
        return appointmentService.schedule(appointment);
    }

    @GetMapping
    @LogAccess(entity = "Appointment", action = "VIEW")
    public List<Appointment> listAll() {
        return appointmentService.getAllAppointments();
    }

    @PutMapping("/{id}")
    @LogAccess(entity = "Appointment", action = "UPDATE")
    public Appointment update(@PathVariable Long id, @RequestBody Appointment appointment) {
        appointment.setId(id);
        return appointmentService.update(appointment);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}


