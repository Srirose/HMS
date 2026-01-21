package com.zaalima.hospital.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
public class TenantResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getTenant();
         System.out.println("Hibernate Tenant = " + tenant);

        if (tenant == null) {
            throw new IllegalStateException("No tenant set in TenantContext");
        }

        return tenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
