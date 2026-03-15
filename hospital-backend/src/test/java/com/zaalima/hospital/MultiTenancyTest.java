package com.zaalima.hospital;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MultiTenancyTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTenantA() throws Exception {
        mockMvc.perform(get("/api/tenant-test").header("X-Tenant-ID", "tenant_a"))
                .andExpect(status().isOk())
                .andExpect(content().string("Accessed schema: tenant_a"));
    }

    @Test
    public void testTenantB() throws Exception {
        mockMvc.perform(get("/api/tenant-test").header("X-Tenant-ID", "tenant_b"))
                .andExpect(status().isOk())
                .andExpect(content().string("Accessed schema: tenant_b"));
    }
}
