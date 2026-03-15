package com.zaalima.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import com.zaalima.hospital.tenant.TenantContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class HospitalBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner seedData(UserService userService, UserRepository userRepository, JdbcTemplate jdbcTemplate) {
		return args -> {
			// Ensure schemas exist
			jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS hospital_a");
			jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS hospital_b");

			String[] tenants = {"hospital_a", "hospital_b"};
			for (String tenant : tenants) {
				TenantContext.setCurrentTenant(tenant);
				
				if (userRepository.findByUsername("admin").isEmpty()) {
					userService.createUser("admin", "admin123", "ADMIN");
					System.out.println("Admin created for " + tenant);
				}
				if (userRepository.findByUsername("doctor").isEmpty()) {
					userService.createUser("doctor", "doctor123", "DOCTOR");
					System.out.println("Doctor created for " + tenant);
				}
				if (userRepository.findByUsername("nurse").isEmpty()) {
					userService.createUser("nurse", "nurse123", "NURSE");
					System.out.println("Nurse created for " + tenant);
				}
				TenantContext.clear();
			}
		};
	}

}
