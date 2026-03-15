package com.zaalima.hospital.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
public class TenantResolver implements CurrentTenantIdentifierResolver {

   @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getCurrentTenant();
        // Agar tenant null hai (Startup ke time), to 'public' return karo
        if (tenant == null) {
            return "public";
        }
        return tenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
