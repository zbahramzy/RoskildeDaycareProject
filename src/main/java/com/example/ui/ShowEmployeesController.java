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

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowEmployeesController implements Initializable {
    @FXML
    private Button back_to_loggedin_button;
    @FXML
    private Button print_button;
    @FXML
    private TableView<Employee> employees_tableview;
    @FXML
    private TableColumn<Employee, Integer> employee_id_table_column;
    @FXML
    private TableColumn<Employee, String> employee_firstname_table_column;
    @FXML
    private TableColumn<Employee, String> employee_lastname_table_column;
    @FXML
    private TableColumn<Employee, String> employee_phone_table_column;
    @FXML
    private TableColumn<Employee, String> employee_title_table_column;
    @FXML
    private TextField keyword_textfield;

    ObservableList<Employee> employeesObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back_to_loggedin_button.setOnAction(event -> Utils.changeScene(event, "loggedin-view.fxml", "Main Menu", null));

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        String employeeViewQuery = "SELECT employee_id, first_name, last_name, phone, title FROM daycare.employees;";

        try {
            connectNow.setPstmt(connectDB.prepareStatement(employeeViewQuery));
            connectNow.setRs(connectNow.getPstmt().executeQuery());
            ResultSet rs = connectNow.getRs();

            //Statement statement = connectDB.createStatement();
            //ResultSet rs = statement.executeQuery(employeeViewQuery);

            while (rs.next()) {
                int id = rs.getInt("employee_id");
                String first_name = rs.getString("employees.first_name");
                String last_name = rs.getString("employees.last_name");
                String phone_number = rs.getString("employees.phone");
                String title = rs.getString("employees.title");

                employeesObservableList.add(new Employee(id, first_name, last_name, phone_number, title));

                employee_id_table_column.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
                employee_firstname_table_column.setCellValueFactory(new PropertyValueFactory<>("first_name"));
                employee_lastname_table_column.setCellValueFactory(new PropertyValueFactory<>("last_name"));
                employee_phone_table_column.setCellValueFactory(new PropertyValueFactory<>("phone"));
                employee_title_table_column.setCellValueFactory(new PropertyValueFactory<>("title"));

                employees_tableview.setItems(employeesObservableList);
            }

            // filter search
            FilteredList<Employee> filteredList = new FilteredList<>(employeesObservableList, p -> true);
            keyword_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(Employee -> {

                    if (newValue.isBlank() || newValue.isEmpty() || newValue == null) {
                        return true;
                    }

                    String searchkeyword = newValue.toLowerCase();

                    if (Employee.getFirst_name().toLowerCase().indexOf(searchkeyword)> -1) {
                        return true;
                    } else if (Employee.getLast_name().toLowerCase().indexOf(searchkeyword)> -1) {
                        return true;
                    } else if (Employee.getPhone().toLowerCase().indexOf(searchkeyword)> -1) {
                        return true;
                    } else
                        return false;
                });
            });
            SortedList<Employee> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(employees_tableview.comparatorProperty());

            //apply filtered and sorted list to tableview
            employees_tableview.setItems(sortedList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
