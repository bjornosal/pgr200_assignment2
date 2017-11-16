package no.salvesen.assignment2;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertiesHandlerTest {

    private PropertiesHandler propertiesHandler;
    private MysqlDataSource dataSource;

    public PropertiesHandlerTest() {
        propertiesHandler = new PropertiesHandler();
        dataSource = new MysqlDataSource();
    }

    @Before
    public void setUp() throws Exception {
        propertiesHandler.setPropertyFilePath("src/test/files/testDatabaseLogin.properties");
    }

    @Test
    public void initializeProperties() throws Exception {
        propertiesHandler.initializeProperties(dataSource);
        assertEquals("localhost", dataSource.getServerName());
        assertEquals("pgr200", dataSource.getUser());
    }


    @Test
    public void setDatabaseNameInProperties() throws Exception {
        propertiesHandler.setDatabaseNameInProperties("pgr200_testing");
        assertEquals("pgr200_testing", propertiesHandler.getDatabaseNameFromProperties());

    }
}