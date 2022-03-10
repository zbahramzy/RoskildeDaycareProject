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
    private Button logout_button;
    @FXML
    private Button go_to_show_children_button;
    @FXML
    private Button go_to_show_parents_button;
    @FXML
    private Button go_to_show_employees_button;
    @FXML
    private Button go_to_show_rooms_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lambda instead of eventhandler
        logout_button.setOnAction(event -> Utils.changeScene(event, "login-view.fxml", "Log in", null));
        go_to_show_children_button.setOnAction(event -> Utils.changeScene(event, "show-children-view.fxml", "Show Children", null));
        go_to_show_parents_button.setOnAction(event -> Utils.changeScene(event, "show-parents-view.fxml", "Show Parents", null));
        go_to_show_employees_button.setOnAction(event -> Utils.changeScene(event, "show-employees-view.fxml", "Show Employees", null));
        go_to_show_rooms_button.setOnAction(event -> Utils.changeScene(event, "show-rooms-view.fxml", "Show Rooms", null));

    }

    public void setUserInformation(String username) {
        welcome_label.setText("Welcome " + username + "!");
    }
}
