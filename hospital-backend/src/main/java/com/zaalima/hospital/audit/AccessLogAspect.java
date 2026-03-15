package com.zaalima.hospital.audit;

import com.zaalima.hospital.tenant.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessLogAspect {

    private final AccessLogRepository accessLogRepository;

    @Before("@annotation(logAccess)")
    public void log(JoinPoint joinPoint, LogAccess logAccess) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        AccessLog log = new AccessLog();
        log.setTenantId(TenantContext.getCurrentTenant());
        log.setUserId(auth != null ? auth.getName() : "anonymous");
        log.setRole(auth != null ? auth.getAuthorities().toString() : "none");
        log.setEntityName(logAccess.entity());
        log.setAction(logAccess.action());
        log.setTimestamp(LocalDateTime.now());
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.setIpAddress(request.getRemoteAddr());
        
        accessLogRepository.save(log);
    }
}
