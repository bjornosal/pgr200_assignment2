package no.salvesen.assignment2;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private MysqlDataSource dataSource;
    private PropertiesHandler propertiesHandler;

    public DatabaseConnection(PropertiesHandler propertiesHandler) {
        dataSource = new MysqlDataSource();
        this.propertiesHandler = propertiesHandler;
    }

    /**
     * Sets up the properties for the datasource.
     *
     * @throws IOException If no file is found or unable to load the InputStream.
     */
    public void initializeProperties() throws IOException {
        propertiesHandler.initializeProperties(dataSource);
    }


    /**
     * Returns a connection to the database.
     * @return A MysqlDataSource Connection.
     * @throws SQLException If unable to get a connection.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void setDataSourceDatabaseName() {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(propertiesHandler.getPropertyFilePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("ERROR IN DATABASECONNECTION");
        }
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
    }
}