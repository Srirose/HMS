package com.zaalima.hospital.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_log")
@Getter
@Setter
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String tenantId;

    @Column(length = 50)
    private String userId;

    @Column(length = 100)
    private String role;

    @Column(length = 100)
    private String entityName;

    private Long entityId;

    @Column(length = 50)
    private String action;

    private LocalDateTime timestamp;

    @Column(length = 50)
    private String ipAddress;
}


