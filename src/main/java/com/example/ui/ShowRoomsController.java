package com.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.transform.Scale;

import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ShowRoomsController implements Initializable {

    //Declaring FXML connectors

    @FXML
    private Button back_to_loggedin_button;
    @FXML
    private Button print_button;
    @FXML
    private TableView<SearchObjects> room_search;
    @FXML
    private TableView<SearchObjects> room_search2;
    @FXML
    private TableColumn<SearchObjects, Integer> class_id_column;
    @FXML
    private TableColumn<SearchObjects, String> child_firstname_column;
    @FXML
    private TableColumn<SearchObjects, String> child_lastname_column;
    @FXML
    private TableColumn<SearchObjects, String> employee_firstname_column;
    @FXML
    private TableColumn<SearchObjects, String> employee_lastname_column;
    @FXML
    private TextField filter_textfield;
    @FXML
    private ComboBox class_combobox;

    //Setting Observable List

    private ObservableList<SearchObjects> SearchObjectsObservableList = FXCollections.observableArrayList();
    private ObservableList<SearchObjects> SearchObjectsObservableList2 = FXCollections.observableArrayList();
    private PreparedStatement statement;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Button to go back
        back_to_loggedin_button.setOnAction(event -> Utils.changeScene(event, "loggedin-view.fxml", "Main Menu", null));
        gigi("1");
        print_button.setCancelButton(true);

        class_combobox.getItems().add("Class 1");
        class_combobox.getItems().add("Class 2");
        class_combobox.getItems().add("Class 3");
        class_combobox.getItems().add("Class 4");
        class_combobox.getSelectionModel().selectFirst();






        //class_combobox.setOnAction(event -> gogo(String.valueOf(class_combobox.getValue().toString().charAt(6))));
        class_combobox.setOnAction(event -> gigi(String.valueOf(class_combobox.getValue().toString().charAt(6))));

//            switch (class_combobox.getValue().toString()) {
//                case "Class 1" -> gogo("1");
//                case "Class 2" -> gogo("2");
//                case "Class 3" -> gogo("3");
//                case "Class 4" -> gogo("4");
//            }



//        //Create Connection
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.getDBconnection();
//
////        //SQL query
//        String show_rooms_query = "Select classes.class_id, children.first_name, children.last_name, employees.first_name, employees.last_name\n" +
//                "FROM daycare.classes, daycare.children, daycare.employees\n" +
//                "WHERE classes.child_id = children.child_id\n" +
//                "AND classes.employee_id = daycare.employees.employee_id\n" +
//                "AND class_id;";
//
//
//        try {
//
//            PreparedStatement statement;
//            statement = connectDB.prepareStatement("Select class_id, children.first_name, children.last_name, employees.first_name, employees.last_name FROM daycare.classes, daycare.children, daycare.employees  WHERE classes.child_id = children.child_id AND classes.employee_id = daycare.employees.employee_id AND class_id = ?");
//            if (class_combobox.getValue() == null){
//                statement.setString(1, "1");
//            } else { statement.setString(1, class_combobox.toString().substring(6));}
//            ResultSet queryOutput = statement.executeQuery();
//            while (queryOutput.next()) {
//
//                Integer query_class_id = queryOutput.getInt("class_id");
//                String query_children_firstname = queryOutput.getString("children.first_name");
//                String query_children_lastname = queryOutput.getString("children.last_name");
//                String query_employee_firstname = queryOutput.getString("employees.first_name");
//                String query_employee_lastname = queryOutput.getString("employees.last_name");
//
//                SearchObjectsObservableList.add(new SearchObjects(query_class_id, query_children_firstname, query_children_lastname));
//                SearchObjectsObservableList2.add(new SearchObjects(query_employee_firstname, query_employee_lastname));
//            }
//
//            class_id_column.setCellValueFactory(new PropertyValueFactory<>("class_id"));
//            child_firstname_column.setCellValueFactory(new PropertyValueFactory<>("child_firstname"));
//            child_lastname_column.setCellValueFactory(new PropertyValueFactory<>("child_lastname"));
//            employee_firstname_column.setCellValueFactory(new PropertyValueFactory<>("employee_firstname"));
//            employee_lastname_column.setCellValueFactory(new PropertyValueFactory<>("employee_lastname"));
//
//            room_search.setItems(SearchObjectsObservableList);
//            room_search2.setItems(SearchObjectsObservableList2);
//
//            //its filter our search
//            FilteredList<SearchObjects> filteredList = new FilteredList<>(SearchObjectsObservableList, p -> true);
//
//            filter_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
//                filteredList.setPredicate(SearchObjects -> {
//                    if (newValue.isBlank() || newValue.isEmpty()) {return true;}
//                    String searchkeyword = newValue.toLowerCase();
//                    if (SearchObjects.getChild_firstname().toLowerCase().contains(searchkeyword)) {
//                        return true;
//                    }
//                    else if (SearchObjects.getChild_lastname().toLowerCase().contains(searchkeyword)) {
//                        return true;
//                    }
//                    else if (SearchObjects.getClass_id().toString().contains(searchkeyword)) {
//                        return true;
//                    }
//                    else if (SearchObjects.getEmployee_firstname().toLowerCase().contains(searchkeyword)) {
//                        return true;
//                    }
//                    else if (SearchObjects.getEmployee_lastname().toLowerCase().contains(searchkeyword)) {
//                        return true;
//                    }
//                    else
//                        return false;
//                });
//
//
//            });
//            SortedList<SearchObjects> sortedList = new SortedList<>(filteredList);
//            sortedList.comparatorProperty().bind(room_search.comparatorProperty());
//
//            //apply filtered and sorted list to tableview
//            room_search.setItems(sortedList);
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @FXML
    private void print(ActionEvent event) {
        //print the tableview

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

        //get printer text responsive

        double scaleX = pageLayout.getPrintableWidth() / room_search.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / room_search.getBoundsInParent().getHeight();
        scaleX = scaleX > 1 ? 1 : scaleX;
        scaleY = scaleY > 1 ? 1 : scaleY;
        //open logo on the top of the tableview
        FileInputStream inputStream = null;

        //make the table looks good on print
        room_search.getTransforms().add(new Scale(scaleX, scaleY));
        //print the tableview
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            boolean printed = job.printPage(pageLayout, room_search);
            if (printed) {
                job.endJob();
            }
        }
    }


    private void gigi(String classid){
        //Create Connection
        SearchObjectsObservableList.clear();
        SearchObjectsObservableList2.clear();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        //SQL query
        String show_rooms_query = "Select classes.class_id, children.first_name, children.last_name, employees.first_name, employees.last_name\n" +
                "FROM daycare.classes, daycare.children, daycare.employees\n" +
                "WHERE classes.child_id = children.child_id\n" +
                "AND classes.employee_id = daycare.employees.employee_id\n" +
                "AND class_id;";


        try {

            PreparedStatement statement;
            statement = connectDB.prepareStatement("Select class_id, children.first_name, children.last_name FROM daycare.classes, daycare.children, daycare.employees  WHERE classes.child_id = children.child_id AND classes.employee_id = daycare.employees.employee_id AND class_id = ?");
                statement.setString(1, classid);

            ResultSet queryOutput = statement.executeQuery();
            while (queryOutput.next()) {

                Integer query_class_id = queryOutput.getInt("class_id");
                String query_children_firstname = queryOutput.getString("children.first_name");
                String query_children_lastname = queryOutput.getString("children.last_name");

                SearchObjectsObservableList.add(new SearchObjects(query_class_id, query_children_firstname, query_children_lastname));
            }

            PreparedStatement statement2;
            statement2 = connectDB.prepareStatement("Select employees.first_name, employees.last_name FROM daycare.employee_class ,employees where employee_class.employee_id = employees.employee_id AND class_id = ?;");
            statement2.setString(1, classid);

            ResultSet queryOutput2 = statement2.executeQuery();
            while (queryOutput2.next()) {

                String query_employee_firstname = queryOutput2.getString("employees.first_name");
                String query_employee_lastname = queryOutput2.getString("employees.last_name");

                SearchObjectsObservableList2.add(new SearchObjects(query_employee_firstname, query_employee_lastname));
            }
            class_id_column.setCellValueFactory(new PropertyValueFactory<>("class_id"));
            child_firstname_column.setCellValueFactory(new PropertyValueFactory<>("child_firstname"));
            child_lastname_column.setCellValueFactory(new PropertyValueFactory<>("child_lastname"));
            employee_firstname_column.setCellValueFactory(new PropertyValueFactory<>("employee_firstname"));
            employee_lastname_column.setCellValueFactory(new PropertyValueFactory<>("employee_lastname"));

            room_search.setItems(SearchObjectsObservableList);
            room_search2.setItems(SearchObjectsObservableList2);

            //its filter our search
            FilteredList<SearchObjects> filteredList = new FilteredList<>(SearchObjectsObservableList, p -> true);

            filter_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(SearchObjects -> {
                    if (newValue.isBlank() || newValue.isEmpty()) {return true;}
                    String searchkeyword = newValue.toLowerCase();

                    if (SearchObjects.getChild_firstname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (SearchObjects.getChild_lastname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (SearchObjects.getClass_id().toString().contains(searchkeyword)) {
                        return true;
                    }
                    else if (SearchObjects.getEmployee_firstname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (SearchObjects.getEmployee_lastname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else
                        return false;
                });


            });
            SortedList<SearchObjects> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(room_search.comparatorProperty());

            //apply filtered and sorted list to tableview
            room_search.setItems(sortedList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void gogo(String classid){
        //Create Connection
        SearchObjectsObservableList.clear();
        SearchObjectsObservableList2.clear();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        //SQL query
        String show_rooms_query = "Select classes.class_id, children.first_name, children.last_name, employees.first_name, employees.last_name\n" +
                "FROM daycare.classes, daycare.children, daycare.employees\n" +
                "WHERE classes.child_id = children.child_id\n" +
                "AND classes.employee_id = daycare.employees.employee_id\n" +
                "AND class_id;";


        try {

            PreparedStatement statement;
            statement = connectDB.prepareStatement("Select class_id, children.first_name, children.last_name, employees.first_name, employees.last_name FROM daycare.classes, daycare.children, daycare.employees  WHERE classes.child_id = children.child_id AND classes.employee_id = daycare.employees.employee_id AND class_id = ?");
            statement.setString(1, classid);

            ResultSet queryOutput = statement.executeQuery();
            while (queryOutput.next()) {

                Integer query_class_id = queryOutput.getInt("class_id");
                String query_children_firstname = queryOutput.getString("children.first_name");
                String query_children_lastname = queryOutput.getString("children.last_name");
                String query_employee_firstname = queryOutput.getString("employees.first_name");
                String query_employee_lastname = queryOutput.getString("employees.last_name");

                SearchObjectsObservableList.add(new SearchObjects(query_class_id, query_children_firstname, query_children_lastname));
                SearchObjectsObservableList2.add(new SearchObjects(query_employee_firstname, query_employee_lastname));
            }

            class_id_column.setCellValueFactory(new PropertyValueFactory<>("class_id"));
            child_firstname_column.setCellValueFactory(new PropertyValueFactory<>("child_firstname"));
            child_lastname_column.setCellValueFactory(new PropertyValueFactory<>("child_lastname"));
            employee_firstname_column.setCellValueFactory(new PropertyValueFactory<>("employee_firstname"));
            employee_lastname_column.setCellValueFactory(new PropertyValueFactory<>("employee_lastname"));

            room_search.setItems(SearchObjectsObservableList);
            room_search2.setItems(SearchObjectsObservableList2);

            //its filter our search
            FilteredList<SearchObjects> filteredList = new FilteredList<>(SearchObjectsObservableList, p -> true);

            filter_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(SearchObjects -> {
                    if (newValue.isBlank() || newValue.isEmpty()) {return true;}
                    String searchkeyword = newValue.toLowerCase();

                    if (SearchObjects.getChild_firstname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (SearchObjects.getChild_lastname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (SearchObjects.getClass_id().toString().contains(searchkeyword)) {
                        return true;
                    }
                    else if (SearchObjects.getEmployee_firstname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else if (SearchObjects.getEmployee_lastname().toLowerCase().contains(searchkeyword)) {
                        return true;
                    }
                    else
                        return false;
                });


            });
            SortedList<SearchObjects> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(room_search.comparatorProperty());

            //apply filtered and sorted list to tableview
            room_search.setItems(sortedList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }



//    @FXML
//    private void class_combo(ActionEvent event) {
//        String vaffa = "Vaffanculo";
//        class_combobox.getSelectionModel().getSelectedItem().toString();
//        switch (class_combobox.getValue().toString()) {
//            case "Class 1" -> {gogo("1");}
//            case "Class 2" -> {gogo("2");}
//            case "Class 3" -> {gogo("3");}
//            case "Class 4" -> {gogo("4");}
//        }
//
//
//    }
}
