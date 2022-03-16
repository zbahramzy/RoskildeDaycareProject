package com.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddToDatabaseController implements Initializable {

    @FXML
    private TextField child_first_name_textfield;
    @FXML
    private TextField child_last_name_textfield;
    @FXML
    private TextField child_date_of_birth_textfield;
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
    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        cancel_button.setOnAction(actionEvent -> stage.close());
    }
    public static void ggg(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AddToDatabaseController.class.getResource("add_to_database.fxml"));
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            Stage stage = new Stage();
            stage.setTitle("Add to Database");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(AddToDatabaseController.class.getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }


    }

}
