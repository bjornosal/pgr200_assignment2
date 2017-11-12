package no.salvesen.assignment2;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private MysqlDataSource dataSource;

    public DatabaseConnection() {
        dataSource = new MysqlDataSource();
    }

    /**
     * Sets up the properties for the datasource.
     *
     * @param propertyFilePath Filepath to a properties file.
     * @throws IOException If no file is found or unable to load the inputstream.
     */
    public void initializeProperties(String propertyFilePath) throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(propertyFilePath);

        properties.load(input);

        dataSource.setServerName(properties.getProperty("serverName"));
//        dataSource.setDatabaseName(properties.getProperty("databaseName"));
        dataSource.setUser(properties.getProperty("databaseUser"));
        dataSource.setPassword(properties.getProperty("databasePassword"));
    }


    /**
     * Returns a connection to the database.
     * @return A MysqlDataSource Connection.
     * @throws SQLException If unable to get a connection.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void setDataSourceDatabaseName(String propertyFilePath) {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(propertyFilePath);
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