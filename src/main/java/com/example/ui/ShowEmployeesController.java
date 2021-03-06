package com.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
//import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Scale;

//import javax.xml.transform.Result;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowEmployeesController implements Initializable {
    @FXML
    private Button back_to_loggedin_button;
    @FXML
    private Button print_button;
    @FXML
    private Button select_date_button;
    @FXML
    private Button reset_date_button;
    @FXML
    private DatePicker my_date_picker;
    @FXML
    private Label my_date_label;
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
    @FXML
    private ToggleGroup toggle_group;
    @FXML
    private RadioButton all_employees;
    @FXML
    private RadioButton working;
    @FXML
    private RadioButton holiday;

    ObservableList<Employee> employeesObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back_to_loggedin_button.setOnAction(event -> Utils.changeScene(event, "loggedin-view.fxml", "Main Menu", null));

        // connecting to the database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        // query to se all employees from the database
        String employeeViewQuery = "SELECT employee_id, first_name, last_name, phone, title FROM daycare.employees;";

        try {
            // send the query to the database
            ResultSet rs = sendQueryToDatabase(connectNow, connectDB, employeeViewQuery, null);

            // show employees from database in tableview
            showEmployees(rs);

            // filter search
            searchFilterEmployeesTable();
        } catch (SQLException e) {
            Logger.getLogger(ShowEmployeesController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } finally {
            connectNow.closeConnection();
        }
    }

    @FXML
    private void show_all_employees() {
        employeesObservableList.clear();

        // connect to database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        String employeeViewQuery = "SELECT employee_id, first_name, last_name, phone, title FROM daycare.employees;";

        try {
            // send the query to the database
            ResultSet rs = sendQueryToDatabase(connectNow, connectDB, employeeViewQuery, null);

            // show employees from database in tableview
            showEmployees(rs);

            // filter search
            searchFilterEmployeesTable();
        } catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } finally {
            connectNow.closeConnection();
        }
    }

    @FXML
    public void select_date_button_handler() {
        LocalDate myDate = my_date_picker.getValue();
        String myFormattedDateOne;

        if (myDate != null) {
            myFormattedDateOne = myDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            my_date_label.setText("" + myFormattedDateOne);

            if(working.isSelected()) {
                get_working_employees();
            } else if(holiday.isSelected()) {
                get_employees_on_holiday();
            }

        } else {
            my_date_label.setText("" + myDate);
        }
    }

    @FXML
    public void reset_date_button_handler() {
        my_date_picker.setValue(null);
        my_date_label.setText("Date");

        if(!all_employees.isSelected()) {
            employeesObservableList.clear();
        }
    }

    @FXML
    private void get_working_employees() {
        employeesObservableList.clear();

        // connect to database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        // query to get those employees that are at work on a given date
        String employeeViewQuery = "SELECT employees.employee_id, employees.first_name, employees.last_name, employees.phone, employees.title " +
                "FROM daycare.employees WHERE employee_id NOT IN (SELECT employees.employee_id FROM daycare.employees " +
                "JOIN daycare.employee_work_schedule ON employees.employee_id=employee_work_schedule.employee_id " +
                "JOIN daycare.work_schedule ON work_schedule.work_schedule_id=employee_work_schedule.work_schedule_id " +
                "WHERE work_schedule.ferie_day = ?);";

        try {
            LocalDate myDate = my_date_picker.getValue();
            String myFormattedDateOne;

            if (myDate != null) {
                // converting the date to string so it can be used in the sql query
                myFormattedDateOne = myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // send the query to the database
                ResultSet rs = sendQueryToDatabase(connectNow, connectDB, employeeViewQuery, myFormattedDateOne);

                // show employees from database in tableview
                showEmployees(rs);

                // filter search
                searchFilterEmployeesTable();
            }
        }  catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } finally {
            connectNow.closeConnection();
        }
    }

    @FXML
    private void get_employees_on_holiday() {
        employeesObservableList.clear();

        // connect to database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        // query to get those employees that are on holiday on a given date
        String employeeViewQuery = "SELECT employees.employee_id, " +
                "employees.first_name, employees.last_name, employees.phone, employees.title FROM daycare.employees " +
                "JOIN daycare.employee_work_schedule ON employees.employee_id=employee_work_schedule.employee_id " +
                "JOIN daycare.work_schedule ON work_schedule.work_schedule_id=employee_work_schedule.work_schedule_id " +
                "WHERE work_schedule.ferie_day = ?;";

        try {
            LocalDate myDate = my_date_picker.getValue();
            String myFormattedDateOne;

            if (myDate != null) {
                // converting the date to string, so it can be used in the sql query
                myFormattedDateOne = myDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // send the query to the database
                ResultSet rs = sendQueryToDatabase(connectNow, connectDB, employeeViewQuery, myFormattedDateOne);

                // show employees from database in tableview
                showEmployees(rs);

                // filter search
                searchFilterEmployeesTable();
            }
        } catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } finally {
            connectNow.closeConnection();
        }
    }

    @FXML
    private void print(){
        //print the tableview and the screen
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

        //get printer text responsive
        double scaleX = pageLayout.getPrintableWidth() / employees_tableview.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / employees_tableview.getBoundsInParent().getHeight();

        scaleX = scaleX > 1 ? 1 : scaleX;
        scaleY = scaleY > 1 ? 1 : scaleY;

        //open logo on the top of the tableview
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src/main/resources/com/example/ui/images/login.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Image image = new Image(inputStream);
        //put the logo on the top of the tableview
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(pageLayout.getPrintableWidth());
        imageView.setFitHeight(pageLayout.getPrintableHeight());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        //make the table looks good on print
        //show the image on the top of the tableview
        employees_tableview.getTransforms().add(new Scale(scaleX, scaleY));

        //print the tableview
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            boolean printed = job.printPage(pageLayout, employees_tableview);
            if (printed) {
                job.endJob();
            }
        }
    }

    // send a query to database method
    private ResultSet sendQueryToDatabase(DatabaseConnection connectNow, Connection connectDB, String query, String input) throws SQLException {
        ResultSet rs;

        connectNow.setPstmt(connectDB.prepareStatement(query));
        if(input != null) {
            connectNow.getPstmt().setString(1, input);
        }
        connectNow.setRs(connectNow.getPstmt().executeQuery());

        return rs = connectNow.getRs();
    }

    // show employees in tableview
    private void showEmployees(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Integer id = rs.getInt("employee_id");
            String firstname = rs.getString("employees.first_name");
            String lastname = rs.getString("employees.last_name");
            String phone = rs.getString("employees.phone");
            String title = rs.getString("employees.title");

            employeesObservableList.add(new Employee(id, firstname, lastname, phone, title));

            employee_id_table_column.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
            employee_firstname_table_column.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            employee_lastname_table_column.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            employee_phone_table_column.setCellValueFactory(new PropertyValueFactory<>("phone"));
            employee_title_table_column.setCellValueFactory(new PropertyValueFactory<>("title"));

            employees_tableview.setItems(employeesObservableList);
        }
    }

    // search filter for tableview
    private void searchFilterEmployeesTable() {
        //FilteredList<Employee> filteredList = new FilteredList<>(employeesObservableList, p -> true);
        FilteredList<Employee> filteredList = new FilteredList<>(employeesObservableList);
        keyword_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(Employee -> {

                if (newValue.isBlank() || newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String search_keyword = newValue.toLowerCase();

                if (Employee.getFirstname().toLowerCase().contains(search_keyword)) {
                    return true;
                } else if (Employee.getLastname().toLowerCase().contains(search_keyword)) {
                    return true;
                } else return Employee.getPhone().toLowerCase().contains(search_keyword);
            });
        });

        SortedList<Employee> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(employees_tableview.comparatorProperty());

        // apply filtered and sorted list to tableview
        employees_tableview.setItems(sortedList);
    }
}