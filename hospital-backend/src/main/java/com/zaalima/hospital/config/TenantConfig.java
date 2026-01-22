package com.zaalima.hospital.config;
import org.springframework.transaction.PlatformTransactionManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;

public class TenantConfig {
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new org.springframework.orm.jpa.JpaTransactionManager(emf);
    }
}
