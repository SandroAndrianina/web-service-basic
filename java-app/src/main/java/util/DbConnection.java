package main.java.util;

import java.sql.*;

public class DbConnection {

    private static final String URL = "jdbc:mariadb://localhost:3306/web_service_basic";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static java.sql.Connection getConnection() throws SQLException {
        java.sql.Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        return connection;
    }

}