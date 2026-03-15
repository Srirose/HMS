package com.zaalima.hospital.tenant;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
@Component
public class SchemaMultiTenantConnectionProvider
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<Object> {

    @Autowired
    private DataSource dataSource;

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource;
    }

    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = super.getConnection(tenantIdentifier);
        if (tenantIdentifier != null && !tenantIdentifier.isBlank()) {
            connection.setSchema(tenantIdentifier);
        } else {
            connection.setSchema("public"); // default schema
        }
        return connection;
    }

    @Override
    protected DataSource selectDataSource(Object tenantIdentifier) {
        return dataSource;
    }
}
