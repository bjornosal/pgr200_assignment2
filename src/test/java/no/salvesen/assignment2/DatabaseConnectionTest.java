package no.salvesen.assignment2;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseConnectionTest {

    private PropertiesHandler propertiesHandler;
    private DatabaseConnection databaseConnection;

    @Before
    public void setUp() throws Exception {
        propertiesHandler = new PropertiesHandler();
        propertiesHandler.setPropertyFilePath("src/test/files/testDatabaseLogin.properties");
        databaseConnection = new DatabaseConnection(propertiesHandler);
        databaseConnection.initializeProperties();
    }

    @Test
    public void initializeProperties() throws Exception {
        databaseConnection.initializeProperties();
        assertEquals(propertiesHandler.getDatabaseNameFromProperties(), "pgr200_assignment_1_testing");
    }

    @Test
    public void getConnection() throws Exception {
        assertNotNull(databaseConnection.getConnection());
    }

    @Test
    public void setDataSourceDatabaseName() throws Exception {
        databaseConnection.setDataSourceDatabaseName();
        assertEquals(propertiesHandler.getDatabaseNameFromProperties(), "pgr200_assignment_1_testing");
    }
}