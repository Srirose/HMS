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
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    @Autowired
    private DataSource dataSource;

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource;
    }

    protected DataSource selectDataSource(String tenantIdentifier) {
        return dataSource;
    }

    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setSchema(tenantIdentifier);
        return connection;
    }

    public void releaseConnection(String tenantIdentifier, Connection connection)
            throws SQLException {
        connection.setSchema("public");
        connection.close();
    }

    @Override
    protected DataSource selectDataSource(Object tenantIdentifier) {
       return dataSource;
    }
}
