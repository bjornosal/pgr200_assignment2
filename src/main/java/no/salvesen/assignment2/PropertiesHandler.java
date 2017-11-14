package no.salvesen.assignment2;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {

    private String propertyFilePath;

    private ExceptionHandler exceptionHandler;
    private boolean isSessionFile;

    public PropertiesHandler() {
        exceptionHandler = new ExceptionHandler();
        isSessionFile = false;
    }

    public void initializeProperties(MysqlDataSource dataSource) throws IOException {
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(propertyFilePath)) {

            properties.load(input);

            dataSource.setServerName(properties.getProperty("serverName"));
            dataSource.setUser(properties.getProperty("databaseUser"));
            dataSource.setPassword(properties.getProperty("databasePassword"));
        }
    }
    public String getPropertyFilePath() {
        return propertyFilePath;
    }

    public void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }

    public String getDatabaseNameFromProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(propertyFilePath)) {
            properties.load(input);
            return properties.getProperty("databaseName");
        }
    }

    protected void setDatabaseNameInProperties(String databaseName) throws IOException {
        Properties properties = new Properties();
        try(InputStream input = new FileInputStream(propertyFilePath);
            FileOutputStream fileOut = new FileOutputStream(propertyFilePath)) {
            properties.load(input);
            properties.setProperty("databaseName", databaseName);
            properties.store(fileOut, "Redefined by user");
        }
    }

    public void waitUntilFileIsClosed() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Wait was interrupted.");
        }
    }

    public void setSessionFile(boolean isSessionFile) {
        this.isSessionFile = isSessionFile;
    }

    public boolean isSessionFile() {
        return isSessionFile;
    }
}
