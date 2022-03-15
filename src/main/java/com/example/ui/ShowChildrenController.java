package com.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowChildrenController implements Initializable {
    @FXML
    private Button back_to_loggedin_button;
    @FXML
    private TableView<ChildrenData> table_view_show_children;
    @FXML
    private TableColumn<ChildrenData, Integer> table_column_id_children_table;
    @FXML
    private TableColumn<ChildrenData, String> table_column_first_name_children_table;
    @FXML
    private TableColumn<ChildrenData, Integer> table_column_last_name_children_table;
    @FXML
    private TableColumn<ChildrenData, LocalDate> table_column_date_of_birth_children_table;
    @FXML
    private TextField text_field_search;
    @FXML
    private ToggleButton toggle_button_waiting_list;

    ObservableList<ChildrenData> childrenDataObservableList = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back_to_loggedin_button.setOnAction(event -> Utils.changeScene(event, "loggedin-view.fxml", "Main Menu", null));

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery("SELECT * FROM children;");
//            String query = "SELECT children.child_id, children.first_name, children.last_name, children.date_of_birth, waiting_order_id, start_year\n" +
//                    "    FROM children\n" +
//                    "    LEFT JOIN waiting_list wl on children.child_id = wl.child_id\n" +
//                    "    LEFT JOIN enrollments e on children.child_id = e.child_id;";


            while (queryOutput.next()) {
                Integer child_id = queryOutput.getInt("child_id");
                String first_name = queryOutput.getString("first_name");
                String last_name = queryOutput.getString("last_name");

               String date1 = queryOutput.getString("date_of_birth");
               String[] date2 = date1.split("-");
               LocalDate date_of_birth = LocalDate.of(Integer.valueOf(date2[0]), Integer.valueOf(date2[1]), Integer.valueOf(date2[2]));

                childrenDataObservableList.add(new ChildrenData(child_id, first_name, last_name, date_of_birth));

                table_column_id_children_table.setCellValueFactory(new PropertyValueFactory<>("id"));
                table_column_first_name_children_table.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                table_column_last_name_children_table.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                table_column_date_of_birth_children_table.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

                table_view_show_children.setItems(childrenDataObservableList);

                FilteredList<ChildrenData> filteredData = new FilteredList<>(childrenDataObservableList, b -> true);

                text_field_search.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(childrenData -> {

                        //no search value
                        if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                            return true;
                        }

                        String searchKeyword = newValue.toLowerCase();

                        if (childrenData.getFirstName().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true; //no match in first name
                        } else if (childrenData.getLastName().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true; //no match in last name
                        }else if (childrenData.getId().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true; //no match in Id
                        }else if (childrenData.getDateOfBirth().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true; //no match in BirthDate
                        } else {
                            return false; //no match at all
                        }
                    });
                });

                SortedList<ChildrenData> sortedData = new SortedList<>(filteredData);
                // bind sorted results with Table View
                sortedData.comparatorProperty().bind(table_view_show_children.comparatorProperty());
                // apply sorting
                table_view_show_children.setItems(sortedData);
            }


        } catch (Exception e){
            e.printStackTrace();
        }






    }
}
