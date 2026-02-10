package com.zaalima.hospital.audit;

import com.zaalima.hospital.tenant.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessLoggingAspect {

    private final AccessLogRepository accessLogRepository;
    private final HttpServletRequest request;

    @Pointcut("@annotation(logAccess)")
    public void logAccessPointcut(LogAccess logAccess) {
    }

    @Around("logAccessPointcut(logAccess)")
    public Object around(ProceedingJoinPoint pjp, LogAccess logAccess) throws Throwable {
        Object result = pjp.proceed();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        String roles = auth != null ? auth.getAuthorities().toString() : "";

        AccessLog log = new AccessLog();
        log.setTenantId(TenantContext.getTenant());
        log.setUserId(username);
        log.setRole(roles);
        log.setEntityName(logAccess.entity());
        log.setAction(logAccess.action());
        log.setTimestamp(LocalDateTime.now());
        log.setIpAddress(request.getRemoteAddr());

        accessLogRepository.save(log);

        return result;
    }
}


