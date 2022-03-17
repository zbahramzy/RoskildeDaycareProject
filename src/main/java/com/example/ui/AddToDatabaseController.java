package com.example.ui;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddToDatabaseController implements Initializable {

    @FXML
    private TextField child_first_name_textfield;
    @FXML
    private TextField child_last_name_textfield;
    @FXML
    private DatePicker child_date_of_birth_datepicker;
    @FXML
    private TextField parent_1_first_name_textfield;
    @FXML
    private TextField parent_1_last_name_textfield;
    @FXML
    private TextField parent_1_phone_number_textfield;
    @FXML
    private TextField parent_2_first_name_textfield;
    @FXML
    private TextField parent_2_last_name_textfield;
    @FXML
    private TextField parent_2_phone_number_textfield;
    @FXML
    private Button cancel_button;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        cancel_button.setOnAction(actionEvent -> stage.close());
    }

    public static void ggg() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AddToDatabaseController.class.getResource("add_to_database.fxml"));
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            Stage add = new Stage();
            add.setTitle("Add to Database");
            add.setScene(scene);
            add.show();


        } catch (IOException e) {
            Logger logger = Logger.getLogger(AddToDatabaseController.class.getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }


    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage add = (Stage) cancel_button.getScene().getWindow();
        add.close();
    }

    @FXML
    public void add_to_db_action(ActionEvent event) {
        //Create Connection
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        //SQL query
        StringBuilder insert_into_db = new StringBuilder();
        PreparedStatement check_person_exist;
        String username = null;
        ResultSet rs = null;
        PreparedStatement Insert = null;

        if (!child_first_name_textfield.getText().trim().isEmpty()
                && !child_last_name_textfield.getText().trim().isEmpty()
                && !parent_1_first_name_textfield.getText().trim().isEmpty()
                && !parent_1_last_name_textfield.getText().trim().isEmpty()
                && !parent_1_phone_number_textfield.getText().trim().isEmpty()
                && !parent_2_first_name_textfield.getText().trim().isEmpty()
                && !parent_2_last_name_textfield.getText().trim().isEmpty()
                && !parent_2_phone_number_textfield.getText().trim().isEmpty()
                && child_date_of_birth_datepicker.getValue() != null) {
        } else {
            System.out.println(child_date_of_birth_datepicker.getValue());
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill all info!");
            alert.show();
        }
        try {
            System.out.println("vaffanculo");
//            System.out.println(child_date_of_birth_datepicker.getValue().toString());
//            check_person_exist = connectDB.prepareStatement("SELECT * FROM  WHERE username = ?");
//
//            check_person_exist.setString(1, username);
//            rs = check_person_exist.executeQuery();
//
//            if (rs.isBeforeFirst()) {
//                System.out.println("User already exixts");
//                Alert alert = new Alert(Alert.AlertType.ERROR, "User already exists");
//                alert.show();
//            } else {
//                Insert = connectDB.prepareStatement("INSERT INTO company.users(username, password, favTeacher) VALUES (?, ?, ?)");
//                Insert.setString(1, username);
//                Insert.setString(2, password);
//                Insert.setString(3, favTeacher);
//                Insert.executeUpdate();
//
//                changeScene(event, "logged-in.fxml", "Welcome!", username, favTeacher);}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

