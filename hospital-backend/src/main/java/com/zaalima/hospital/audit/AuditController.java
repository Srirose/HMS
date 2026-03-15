package com.zaalima.hospital.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuditController {

    private final AccessLogRepository accessLogRepository;

    @GetMapping("/logs")
    @LogAccess(entity = "AuditLog", action = "VIEW")
    public List<AccessLog> getLogs() {
        return accessLogRepository.findAll();
    }
}
