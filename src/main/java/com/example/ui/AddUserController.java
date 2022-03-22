package com.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddUserController implements Initializable {
    @FXML
    private TextField new_username_textfield;
    @FXML
    private PasswordField new_user_passwordfield;
    @FXML
    private PasswordField new_user_password_check_passwordfield;
    @FXML
    private Button cancel_button;

    private boolean go = true;
    private PreparedStatement insert;
    private PreparedStatement check_database;
    private ResultSet rs = null;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//       cancel_button.setOnAction(actionEvent -> stage.close());
    }

    public static void AddUser() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AddFamilyController.class.getResource("add_user.fxml"));
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            Stage add = new Stage();
            add.setTitle("Add User");
            add.setScene(scene);
            add.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(AddFamilyController.class.getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    @FXML
    public void on_cancel_button(ActionEvent event) {
        Stage add = (Stage) cancel_button.getScene().getWindow();
        add.close();
    }

    @FXML
    public void add_new_user_btn(ActionEvent event) {
        //Create Connection
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();
        go = true;

        try {
            //Check if User exist already
            if (go){
                if(new_username_textfield.getText().isEmpty()
                        || new_user_passwordfield.getText().isEmpty()
                        || new_user_password_check_passwordfield.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fill all informations, Please");
                    alert.show();
                    go = false;
                }
            }
            if (go) {
                check_database = connectDB.prepareStatement("select username From users where username = ?");
                check_database.setString(1, new_username_textfield.getText());
                rs = check_database.executeQuery();
                if (rs.isBeforeFirst()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User already exists");
                    alert.show();
                    go = false;
                }
            }
            //Check if all passwords are equals
            if (go) {
                if (!new_user_password_check_passwordfield.getText().equals(new_user_passwordfield.getText())){
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Passwords don't match, please retry");
                    alert.show();
                }
            }
            //Insert User into DB
            if (go) {
                PreparedStatement insert = connectDB.prepareStatement("INSERT INTO users(username,password) VALUE (?,?)");
                insert.setString(1, new_username_textfield.getText());
                insert.setString(2, new_user_passwordfield.getText());
                insert.executeUpdate();
            }
            //Message User Added
            if (go){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Added "+ new_username_textfield.getText()+" to Database", ButtonType.CLOSE);
            alert.show();}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
