package com.example.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Label welcome_label;
    @FXML
    //private Button logout_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.changeScene(event, "main.fxml", "Log in", null);
            }
        });
         */
    }

    public void setUserInformation(String username) {
        welcome_label.setText("Welcome " + username + "!");
    }
}
