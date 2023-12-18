package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils implements AutoCloseable {

    private static Connection con;

    public static Connection connect() {
        try {
            Class.forName ("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println ("Database Class not Found: ");
            e.printStackTrace();
            return null;
        }

        try {
            con = DriverManager.getConnection(
                System.getProperty("dbUrl"),
                System.getProperty("dbUser"),
                System.getProperty("dbPassword")
            );
            return con;

        } catch (SQLException e) {
            System.out.println("Error while connecting to Database: ");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        con.close();
    }
}
