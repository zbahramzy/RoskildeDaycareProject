package com.example.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {

    public Connection databaselink;

    public Connection getDBconnection(){
        String url = System.getenv("url");
        String username = System.getenv("username");
        String password = System.getenv("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaselink = DriverManager.getConnection(url, username, password);
        } catch (Exception e){
            e.printStackTrace();
        }
        return databaselink;
    }
}
