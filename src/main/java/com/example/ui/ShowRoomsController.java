package com.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowRoomsController implements Initializable {

    //Declaring FXML connectors

    @FXML
    private Button back_to_loggedin_button;
    @FXML
    private TableView<ShowRoomsObjects> room_search;
    @FXML
    private TableColumn<ShowRoomsObjects, Integer> class_id_column;
    @FXML
    private TableColumn<ShowRoomsObjects, String> child_firstname_column;
    @FXML
    private TableColumn<ShowRoomsObjects, String> child_lastname_column;
    @FXML
    private TableColumn<ShowRoomsObjects, String> teacher_firstname_column;
    @FXML
    private TableColumn<ShowRoomsObjects, String> teacher_lastname_column;
    @FXML
    private TextField filter_textfield;

    //Setting Observable List

    ObservableList<ShowRoomsObjects> showRoomsObjectsObservableList = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Button to go back
        back_to_loggedin_button.setOnAction(event -> Utils.changeScene(event, "loggedin-view.fxml", "Main Menu", null));

        //Create Connection
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        //SQL query
        String show_rooms_query = "Select classes.class_id, children.first_name, children.last_name, employees.first_name, employees.last_name\n" +
                "FROM daycare.classes, daycare.children, daycare.employees\n" +
                "WHERE classes.child_id = children.child_id\n" +
                "AND classes.employee_id = daycare.employees.employee_id\n" +
                "ORDER BY class_id;";
        try {

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(show_rooms_query);

            while (queryOutput.next()) {

                Integer query_class_id = queryOutput.getInt("class_id");
                String query_children_firstname = queryOutput.getString("children.first_name");
                String query_children_lastname = queryOutput.getString("children.last_name");
                String query_teacher_firstname = queryOutput.getString("employees.first_name");
                String query_teacher_lastname = queryOutput.getString("employees.last_name");


                showRoomsObjectsObservableList.add(new ShowRoomsObjects(query_class_id, query_children_firstname, query_children_lastname, query_teacher_firstname, query_teacher_lastname));
            }

            class_id_column.setCellValueFactory(new PropertyValueFactory<>("class_id"));
            child_firstname_column.setCellValueFactory(new PropertyValueFactory<>("child_firstname"));
            child_lastname_column.setCellValueFactory(new PropertyValueFactory<>("child_lastname"));
            teacher_firstname_column.setCellValueFactory(new PropertyValueFactory<>("teacher_firstname"));
            teacher_lastname_column.setCellValueFactory(new PropertyValueFactory<>("teacher_lastname"));

            room_search.setItems(showRoomsObjectsObservableList);

            //its filter our search
            FilteredList<ShowRoomsObjects> filteredList = new FilteredList<>(showRoomsObjectsObservableList, p -> true);

            filter_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(ShowRoomsObject -> {
                    if (newValue.isBlank() || newValue.isEmpty()) {
                        return true;

                    }
                    String searchkeyword = newValue.toLowerCase();
                    if (ShowRoomsObject.getChild_firstname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (ShowRoomsObject.getChild_lastname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (ShowRoomsObject.getTeacher_firstname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (ShowRoomsObject.getTeacher_lastname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else
                        return false;
                });


            });
            SortedList<ShowRoomsObjects> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(room_search.comparatorProperty());

            //apply filtered and sorted list to tableview
            room_search.setItems(sortedList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
