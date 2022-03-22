package com.example.ui;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private Button button_print_children;
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
    private RadioButton radio_button_children_all;
    @FXML
    private RadioButton radio_button_children_waiting_list;
    @FXML
    private ToggleGroup toggle_group;
    @FXML
    private TextField text_field_display;
    @FXML
    private PieChart piechart2;

    ObservableList<ChildrenData> childrenDataObservableList = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back_to_loggedin_button.setOnAction(event -> Utils.changeScene(event, "loggedin-view.fxml", "Main Menu", null));

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();
        String query = "SELECT children.child_id, children.first_name, children.last_name, children.date_of_birth, waiting_order_id, start_year\n" +
                "    FROM children\n" +
                "    LEFT JOIN waiting_list wl on children.child_id = wl.child_id\n" +
                "    LEFT JOIN enrollments e on children.child_id = e.child_id;";


        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);


            while (queryOutput.next()) {
                Integer child_id = queryOutput.getInt("child_id");
                String first_name = queryOutput.getString("first_name");
                String last_name = queryOutput.getString("last_name");
                //getting and converting date
                String date1 = queryOutput.getString("date_of_birth");
                String[] date2 = date1.split("-");
                LocalDate date_of_birth = LocalDate.of(Integer.valueOf(date2[0]), Integer.valueOf(date2[1]), Integer.valueOf(date2[2]));
                String is_enrolled_value = queryOutput.getString("start_year");

                childrenDataObservableList.add(new ChildrenData(child_id, first_name, last_name, date_of_birth));

                table_column_id_children_table.setCellValueFactory(new PropertyValueFactory<>("id"));
                table_column_first_name_children_table.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                table_column_last_name_children_table.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                table_column_date_of_birth_children_table.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

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
//            connectDB.close();
//            connectNow.closeConnection();
        } catch (Exception e){
            e.printStackTrace();
        }
        //for the piechart

//        DatabaseConnection connectNowPie = new DatabaseConnection();
//        Connection connectDBPie = connectNowPie.getDBconnection();
        String query_for_pie = "select count(start_year) as n1 from enrollments\n" +
                "union\n" +
                "select count(date_of_birth) as n2 from children;";

        try {

            Statement statement1 = connectDB.createStatement();
            ResultSet result_set_query_for_pie_all = statement1.executeQuery(query_for_pie);
            int p1 = 0;
            int p2 = 0;
            int enrollment_count = 0;
            int all_children_count = 0;
            ArrayList<Integer> numbers = new ArrayList<>();

            while (result_set_query_for_pie_all.next()) {
                numbers.add(result_set_query_for_pie_all.getInt("n1"));
                }

            enrollment_count = numbers.get(0);
            all_children_count = numbers.get(1);

            p1 = (((all_children_count - enrollment_count) *100 / all_children_count));
            p2 = 100-p1;
            pieChartData2.add(new PieChart.Data("Waiting List", p1));
            pieChartData2.add(new PieChart.Data("Enrolled ", p2));
            piechart2.setData(pieChartData2);
            piechart2.setStartAngle(90);
            //make a pichart animated
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(5000), piechart2);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
        } catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }
    @FXML
    private void handle_waiting_list(ActionEvent event) {
        childrenDataObservableList.clear();

        DatabaseConnection Connectnow2 = new DatabaseConnection();
        Connection connectDB2 = Connectnow2.getDBconnection();
        String query = "SELECT children.child_id, children.first_name, children.last_name, children.date_of_birth, waiting_order_id\n" +
                "    FROM children\n" +
                "    JOIN waiting_list wl on children.child_id = wl.child_id;";

        try {
            Statement statement = connectDB2.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                Integer child_id = queryOutput.getInt("child_id");
                String first_name = queryOutput.getString("first_name");
                String last_name = queryOutput.getString("last_name");
                //getting and converting date
                String date1 = queryOutput.getString("date_of_birth");
                String[] date2 = date1.split("-");
                LocalDate date_of_birth = LocalDate.of(Integer.valueOf(date2[0]), Integer.valueOf(date2[1]), Integer.valueOf(date2[2]));
                //publish the observable list

                childrenDataObservableList.add(new ChildrenData(child_id, first_name, last_name, date_of_birth));


                table_column_id_children_table.setCellValueFactory(new PropertyValueFactory<>("id"));
                table_column_first_name_children_table.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                table_column_last_name_children_table.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                table_column_date_of_birth_children_table.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

                FilteredList<ChildrenData> filteredData = new FilteredList<>(childrenDataObservableList, b -> true);

                text_field_search.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(childrenData -> {

                //no search value
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (childrenData.getFirstName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; //no match in first name
                } else if (childrenData.getLastName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; //no match in last name
                } else if (childrenData.getId().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; //no match in Id
                } else if (childrenData.getDateOfBirth().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; //no match in BirthDate
                } else {
                    return false; //no match at all
                }
                }));
                SortedList<ChildrenData> sortedData = new SortedList<>(filteredData);
                // bind sorted results with Table View
                sortedData.comparatorProperty().bind(table_view_show_children.comparatorProperty());
                // apply sorting
                table_view_show_children.setItems(sortedData);
            }
        } catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    @FXML
    private void back_all(ActionEvent event) {
        childrenDataObservableList.clear();

        DatabaseConnection Connectnow3 = new DatabaseConnection();
        Connection connectDB3 = Connectnow3.getDBconnection();
        String query = "SELECT children.child_id, children.first_name, children.last_name, children.date_of_birth\n" +
                "    FROM children;";

        try {
            Statement statement = connectDB3.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                Integer child_id = queryOutput.getInt("child_id");
                String first_name = queryOutput.getString("first_name");
                String last_name = queryOutput.getString("last_name");
                //getting and converting date
                String date1 = queryOutput.getString("date_of_birth");
                String[] date2 = date1.split("-");
                LocalDate date_of_birth = LocalDate.of(Integer.valueOf(date2[0]), Integer.valueOf(date2[1]), Integer.valueOf(date2[2]));
                //publish the observable list

                childrenDataObservableList.add(new ChildrenData(child_id, first_name, last_name, date_of_birth));


                table_column_id_children_table.setCellValueFactory(new PropertyValueFactory<>("id"));
                table_column_first_name_children_table.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                table_column_last_name_children_table.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                table_column_date_of_birth_children_table.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

                FilteredList<ChildrenData> filteredData = new FilteredList<>(childrenDataObservableList, b -> true);

                text_field_search.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(childrenData -> {

                    //no search value
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if (childrenData.getFirstName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //no match in first name
                    } else if (childrenData.getLastName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //no match in last name
                    } else if (childrenData.getId().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //no match in Id
                    } else if (childrenData.getDateOfBirth().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //no match in BirthDate
                    } else {
                        return false; //no match at all
                    }
                }));
                SortedList<ChildrenData> sortedData = new SortedList<>(filteredData);
                // bind sorted results with Table View
                sortedData.comparatorProperty().bind(table_view_show_children.comparatorProperty());
                // apply sorting
                table_view_show_children.setItems(sortedData);
            }
        } catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    @FXML
    private void print (ActionEvent event){
        //print the tableview

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

        //get printer text responsive

        double scaleX = pageLayout.getPrintableWidth() / table_view_show_children.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / table_view_show_children.getBoundsInParent().getHeight();
        scaleX = scaleX > 1 ? 1 : scaleX;
        scaleY = scaleY > 1 ? 1 : scaleY;
        //open logo on the top of the tableview
        FileInputStream inputStream = null;

        //make the table looks good on print
        table_view_show_children.getTransforms().add(new Scale(scaleX, scaleY));
        //print the tableview
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            boolean printed = job.printPage(pageLayout, table_view_show_children);
            if (printed) {
                job.endJob();
            }
        }
    }

    @FXML
    private void mouse_clicked_select_row(MouseEvent event){
        ChildrenData selectedChildrenData = table_view_show_children.getSelectionModel().getSelectedItem();
        if(selectedChildrenData != null){
            DatabaseConnection Connectnow4 = new DatabaseConnection();
            Connection connectDB4 = Connectnow4.getDBconnection();
            String query = "SELECT parents.first_name, parents.last_name, parents.phone FROM parents\n" +
                    "    JOIN relations r on parents.parent_id = r.parent_id\n" +
                    "    RIGHT JOIN children c on r.child_id = c.child_id\n" +
                    "    WHERE c.child_id =" + selectedChildrenData.getId() + ";";
            ArrayList<SearchObjects> parentsToDisplay = new ArrayList<>();

            try {
                Statement statement = connectDB4.createStatement();
                ResultSet queryOutput = statement.executeQuery(query);
                while (queryOutput.next()) {
                    String parent_firstname = queryOutput.getString("parents.first_name");
                    String parent_lasttname = queryOutput.getString("parents.last_name");
                    String parent_phone = queryOutput.getString("parents.phone");

                    SearchObjects parent = new SearchObjects(parent_firstname, parent_lasttname, parent_phone);
                    parentsToDisplay.add(parent);
                }
            }catch (SQLException e) {
                Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
                e.printStackTrace();
            }
            String textToDisplay;
            StringBuilder sb = new StringBuilder();
            for (SearchObjects sO : parentsToDisplay
                 ) { sb.append(sO.myToString());

            }
            text_field_display.setText(sb.toString());

            text_field_display.setStyle("-fx-text-fill: #0f123f;");
            text_field_display.setStyle("-fx-background-color: #fff;");
            text_field_display.setStyle("-fx-background-radius: 10;");
            text_field_display.setStyle("-fx-border-color: #0f123f;");
            text_field_display.setStyle("-fx-border-radius: 10;");
            text_field_display.setStyle("-fx-border-width: 2;");
            text_field_display.setStyle("-fx-font-size: 16;");
            text_field_display.setStyle("-fx-font-weight: bold;");
            //set style as animation for the text
            FadeTransition ft = new FadeTransition(Duration.millis(2000), text_field_display);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);

            //make a loop to show the text for 3 seconds
            ft.play();

            //make it visible everytime is clicked and desappear when clicked again
            text_field_display.setVisible(true);

            // hide the text after 5 seconds
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            text_field_display.setVisible(false);

                        }
                    }
                    , 10000);
        }
        else{
            //make screen color transparent
            text_field_display.setStyle("-fx-background-color: transparent;");
            text_field_display.setText("");
            text_field_display.clear();
            text_field_display.setVisible(false);

            //chanege background to #0f123f in e
        }
        //make translate transition for the vbox
    }
}
