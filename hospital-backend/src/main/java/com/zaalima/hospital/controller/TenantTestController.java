package com.zaalima.hospital.controller;

import com.zaalima.hospital.tenant.TenantContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenant-test")
@CrossOrigin(origins = "http://localhost:4200")
public class TenantTestController {

    @GetMapping
    public String testTenant() {
        return "Accessed schema: " + TenantContext.getCurrentTenant();
    }
}
