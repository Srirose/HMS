package com.zaalima.hospital.tenant;

import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class TenantFilter extends OncePerRequestFilter {

    @Override
        protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Allow OPTIONS (CORS pre-flight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String tenant = request.getHeader("X-Tenant-ID");

        if (tenant == null || tenant.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "X-Tenant-ID header missing");
            return;
        }

        // FIX: Convert 'hospital-a' -> 'hospital_a' to match Postgres Schema
        String formattedTenant = tenant.replace("-", "_");

        TenantContext.setCurrentTenant(formattedTenant);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}