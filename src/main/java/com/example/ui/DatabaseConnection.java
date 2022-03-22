package com.example.ui;

import java.sql.*;

public class DatabaseConnection {
    // connect to database
    public Connection databaselink;
    private PreparedStatement pstmt = null;
    private PreparedStatement psInsert = null;
    private PreparedStatement psCheckUserExists = null;
    private ResultSet rs = null;
    // constructors
    public DatabaseConnection() {}
    // getters
    public Connection getDatabaselink() {
        return databaselink;
    }
    public PreparedStatement getPstmt() {
        return pstmt;
    }
    public PreparedStatement getPsInsert() {
        return psInsert;
    }
    public PreparedStatement getPsCheckUserExists() {
        return psCheckUserExists;
    }
    public ResultSet getRs() {
        return rs;
    }
    // setters
    public void setDatabaselink(Connection databaselink) {
        this.databaselink = databaselink;
    }
    public void setPstmt(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }
    public void setPsInsert(PreparedStatement psInsert) {
        this.psInsert = psInsert;
    }
    public void setPsCheckUserExists(PreparedStatement psCheckUserExists) {
        this.psCheckUserExists = psCheckUserExists;
    }
    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    // database connection and returns the connection
    public Connection getDBconnection(){
        String url = System.getenv("url");
        String username = System.getenv("username");
        String password = System.getenv("password");
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            databaselink = DriverManager.getConnection(url, username, password);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return databaselink;
    }

    public void closeConnection() {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (psCheckUserExists != null) {
            try {
                psCheckUserExists.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (psInsert != null) {
            try {
                psInsert.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (getDBconnection() != null) {
            try {
                getDBconnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
