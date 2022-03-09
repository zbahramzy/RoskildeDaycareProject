package com.example.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private TextField username_text_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button login_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.loginUser(event, username_text_field.getText(), password_field.getText());
                username_text_field.setText("");
                password_field.setText("");
                username_text_field.requestFocus();
            }
        });

    }
}