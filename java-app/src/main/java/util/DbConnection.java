package main.java.util;

import java.sql.*;

public class DbConnection {

    private static final String URL = "jdbc:mariadb://localhost:3306/web_service_basic";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

public static Connection getConnection() throws SQLException {
    try {
        Class.forName("org.mariadb.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        throw new SQLException("MariaDB driver not found in WEB-INF/lib", e);
    }
    return DriverManager.getConnection(URL, USERNAME, PASSWORD);
}

}