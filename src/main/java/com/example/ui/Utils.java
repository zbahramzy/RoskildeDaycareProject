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


    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username)  {

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
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();
        try {
            // connecting to the database
            //connection();

            connectNow.setPstmt(connectDB.prepareStatement("SELECT password FROM users WHERE username = ?"));
            connectNow.getPstmt().setString(1, username);
            connectNow.setRs(connectNow.getPstmt().executeQuery());
            connectNow.getRs();
            //pstmt = connect.prepareStatement("SELECT password FROM daycare.users WHERE username = ?");
            //pstmt.setString(1, username);
            //rs = pstmt.executeQuery();

            if (!connectNow.getRs().isBeforeFirst()) {
                System.out.println("User not found");
                Alert alert = new Alert(Alert.AlertType.ERROR, "User not found");
                alert.show();
            } else {
                while (connectNow.getRs().next()) {
                    String retrievedPassword = connectNow.getRs().getString("password");

                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "loggedin-view.fxml", "Welcome!", username);
                    } else {
                        System.out.println("Password didn't match");
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Password didn't match");
                        alert.show();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectNow.closeConnection();
        }
    }
}
