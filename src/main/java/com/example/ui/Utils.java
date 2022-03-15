package com.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;

public class Utils {

    // connect to database
    private static Connection connect = null;
    private static String url = System.getenv("url");
    private static String username = System.getenv("username");
    private static String password = System.getenv("password");

    // communicate to database
    private static PreparedStatement pstmt = null;
    private static PreparedStatement psInsert = null;
    private static PreparedStatement psCheckUserExists = null;
    private static ResultSet rs = null;


    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {

        Parent root = null;

        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(Utils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }



    // logs in the user from gui
    public static void loginUser(ActionEvent event, String username, String password) {
        try {
            // connecting to the database
            connection();

            pstmt = connect.prepareStatement("SELECT password FROM daycare.users WHERE username = ?");
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("User not found");
                Alert alert = new Alert(Alert.AlertType.ERROR, "User not found");
                alert.show();
            } else {
                while (rs.next()) {
                    String retrievedPassword = rs.getString("password");

                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "loggedin-view.fxml", "Welcome!", username);
                    } else {
                        System.out.println("Password didn't match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.show();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void connection() {
        try {
            connect = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {

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

        if (connect != null) {
            try {
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
