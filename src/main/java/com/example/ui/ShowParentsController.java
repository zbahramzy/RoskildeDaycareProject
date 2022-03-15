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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Scale;

import javax.xml.transform.Result;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowParentsController implements Initializable {
    @FXML
    private Button back_to_loggedin_button;
    @FXML
    private Button print_parents;
    @FXML
    private TableView<ParentsSearchModel> parents_tableview;

    @FXML
    private TableColumn<ParentsSearchModel, Integer> id_parents_coulumn;
    @FXML
    private TableColumn<ParentsSearchModel, String> firstname_parents_column;
    @FXML
    private TableColumn<ParentsSearchModel, String> lastname_parents_column;
    @FXML
    private TableColumn<ParentsSearchModel, String> phone_parents_column;
    @FXML
    private TextField keyword_textfield;
    @FXML
    private RadioButton parents_waiting_list;

    ObservableList<ParentsSearchModel> parentsSearchModelObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        back_to_loggedin_button.setOnAction(event -> Utils.changeScene(event, "loggedin-view.fxml", "Main Menu", null));
        parents_waiting_list.setOnAction(event -> handle_waitinglist(event));
        print_parents.setCancelButton(true);
        parents_tableview.setEditable(true);


        DatabaseConnection Connectnow2 = new DatabaseConnection();
        Connection connectDB = Connectnow2.getDBconnection();


        String parents_query = "SELECT parents.parent_id,parents.first_name,parents.last_name,parents.phone FROM daycare.parents;";


//set action on query and make a tableview


        //String waiting_list_query="SElECT parents.parent_id,parents.first_name,parents.last_name,parents.phone FROM parents" +
        //         " left join relations on parents.parent_id=relations.parent_id join enrollments on relations.child_id=enrollments.child_id where relations.child_id=enrollments.child_id;";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryoutput = statement.executeQuery(parents_query);
            //ResultSet waiting_list_queryoutput=statement.executeQuery(waiting_list_query);
            //show waiting_list on radiobuttom


//run two queries and make a tableview in one loop

            while (queryoutput.next()) {
                Integer id_parents_coulumn = queryoutput.getInt("parent_id");
                String firstname_parents_column = queryoutput.getString("first_name");
                String lastname_parents_column = queryoutput.getString("last_name");
                String phone = queryoutput.getString("phone");
                //publish the observable list
                parentsSearchModelObservableList.add(new ParentsSearchModel(id_parents_coulumn, firstname_parents_column, lastname_parents_column, phone));
            }


            id_parents_coulumn.setCellValueFactory(new PropertyValueFactory<>("parent_id"));
            firstname_parents_column.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            lastname_parents_column.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            phone_parents_column.setCellValueFactory(new PropertyValueFactory<>("phone"));

            parents_tableview.setItems(parentsSearchModelObservableList);
            //run the quarry of waiting list


            //its filter our search
            FilteredList<ParentsSearchModel> filteredList = new FilteredList<>(parentsSearchModelObservableList, p -> true);
            keyword_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(parentsSearchModel -> {
                    if (newValue.isBlank() || newValue.isEmpty() || newValue == null) {
                        return true;

                    }

                    String searchkeyword = newValue.toLowerCase();
                    if (parentsSearchModel.getFirst_name().toLowerCase().indexOf(searchkeyword) > -1) {
                        return true;
                    } else if (parentsSearchModel.getLast_name().toLowerCase().indexOf(searchkeyword) > -1) {
                        return true;
                    } else if (parentsSearchModel.getPhone().toLowerCase().indexOf(searchkeyword) > -1) {
                        return true;
                    } else
                        return false;
                });


            });
            SortedList<ParentsSearchModel> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(parents_tableview.comparatorProperty());

            //apply filtered and sorted list to tableview
            parents_tableview.setItems(sortedList);


        } catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    @FXML
    private void handle_waitinglist(ActionEvent event) {
        DatabaseConnection Connectnow3 = new DatabaseConnection();
        Connection connectDB1 = Connectnow3.getDBconnection();
        String waiting_list_query = "SElECT parents.parent_id,parents.first_name,parents.last_name,parents.phone FROM daycare.parents left join daycare.relations on parents.parent_id=relations.parent_id join daycare.waiting_list on waiting_list.child_id=relations.child_id where relations.child_id=waiting_list.child_id;";

        try {
            Statement statement = connectDB1.createStatement();
            ResultSet waiting_list_queryoutput = statement.executeQuery(waiting_list_query);
            while (waiting_list_queryoutput.next()) {
                //print pdf of result of the query





            }











            //show waiting_list on radiobuttom
        } catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        //run radio buttem on parents waiting list with connection of sql

    }
    @FXML
    private void print(ActionEvent event) {
        //print the tableview

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        //high quality print




        //get printer text responsive

        double scaleX = pageLayout.getPrintableWidth() / parents_tableview.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / parents_tableview.getBoundsInParent().getHeight();
        scaleX = scaleX > 1 ? 1 : scaleX;
        scaleY = scaleY > 1 ? 1 : scaleY;
        //open logo on the top of the tableview
        FileInputStream inputStream = null;



        //make the table looks good on print
        parents_tableview.getTransforms().add(new Scale(scaleX, scaleY));
        //print the tableview
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            boolean printed = job.printPage(pageLayout, parents_tableview);
            if (printed) {
                job.endJob();
            }
        }
    }


            }

