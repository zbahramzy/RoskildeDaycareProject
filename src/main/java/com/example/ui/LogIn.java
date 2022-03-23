package com.example.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LogIn extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //creat a new scene for the parent window


        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add("src/main/java/com/example/ui/cssparents.css");

//        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("add_family.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
//        stage.setScene(scene);
//        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}