package com.bus.busmanagement;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnection {
    private static String host = "jdbc:mysql://localhost:3306/bus_management";
    private static final int port = 3306;
    private static String user = "root";
    private static String password = "hoang1234";
    private static Connection connection;

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = java.sql.DriverManager.getConnection(host, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

