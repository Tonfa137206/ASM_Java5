package com.poly.assigment_java5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws SQLException {
        // Test that DataSource is injected properly
        assertNotNull(dataSource, "DataSource should not be null");
        
        // Test that we can get a valid connection
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
            assertTrue(connection.isValid(5), "Connection should be valid within 5 seconds");
            
            // Optional: Test basic database metadata
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            assertNotNull(databaseProductName, "Database product name should not be null");
            
            System.out.println("Database connection successful!");
            System.out.println("Database: " + databaseProductName);
            System.out.println("URL: " + connection.getMetaData().getURL());
        }
    }
}