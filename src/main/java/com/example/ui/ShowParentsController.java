package com.example.ui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import javax.xml.transform.Result;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    private TextField screen;
    @FXML
    private RadioButton parents_waiting_list;
    @FXML
    private RadioButton parents_all;


    @FXML
            private ToggleGroup toggle_group;
    @FXML
    private VBox vbox;
    private Parent fxml;

    ObservableList<ParentsSearchModel> parentsSearchModelObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        back_to_loggedin_button.setOnAction(event -> Utils.changeScene(event, "loggedin-view.fxml", "Main Menu", null));
        parents_waiting_list.setOnAction(event -> handle_waitinglist(event));
        parents_all.setOnAction(event -> back_all(event));
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
        parentsSearchModelObservableList.clear();

        DatabaseConnection Connectnow3 = new DatabaseConnection();
        Connection connectDB1 = Connectnow3.getDBconnection();
        String waiting_list_query = "SElECT parents.parent_id,parents.first_name,parents.last_name,parents.phone FROM daycare.parents left join daycare.relations on parents.parent_id=relations.parent_id join daycare.waiting_list on waiting_list.child_id=relations.child_id where relations.child_id=waiting_list.child_id;";

        try {
            Statement statement = connectDB1.createStatement();
            ResultSet queryoutput = statement.executeQuery(waiting_list_query);
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

                //print pdf of result of the query
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
    private void back_all(ActionEvent event) {
        parentsSearchModelObservableList.clear();
        DatabaseConnection Connectnow4 = new DatabaseConnection();
        Connection connectDB2 = Connectnow4.getDBconnection();
        String queryoutput = "SELECT parents.parent_id,parents.first_name,parents.last_name,parents.phone FROM daycare.parents;";

        try {
            Statement statement = connectDB2.createStatement();
            ResultSet query = statement.executeQuery(queryoutput);
            while (query.next()) {
                Integer id_parents_coulumn = query.getInt("parent_id");
                String firstname_parents_column = query.getString("first_name");
                String lastname_parents_column = query.getString("last_name");
                String phone = query.getString("phone");
                //publish the observable list
                parentsSearchModelObservableList.add(new ParentsSearchModel(id_parents_coulumn, firstname_parents_column, lastname_parents_column, phone));
            }
            id_parents_coulumn.setCellValueFactory(new PropertyValueFactory<>("parent_id"));
            firstname_parents_column.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            lastname_parents_column.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            phone_parents_column.setCellValueFactory(new PropertyValueFactory<>("phone"));

            parents_tableview.setItems(parentsSearchModelObservableList);

            //print pdf of result of the query
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



        } catch (SQLException e) {
            Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }
    //when hover over the tableview show the information about that person
    @FXML
    private void mouse_over(MouseEvent event){
        ParentsSearchModel selected_parents = parents_tableview.getSelectionModel().getSelectedItem();
        if(selected_parents != null){
            DatabaseConnection Connectnow4 = new DatabaseConnection();
            Connection connectDB4 = Connectnow4.getDBconnection();
            String queryoutput = " SELECT children.first_name, children.last_name, children.date_of_birth from children\n" +
                    "                       left JOIN relations r on children.child_id = r.child_id\n" +
                    "                        JOIN  parents on r.parent_id = parents.parent_id \n" +
                    "                      WHERE parents.parent_id ="+selected_parents.getParent_id()+";";
            ArrayList<String> childrenDesplay = new ArrayList<>();
            try {
                Statement statement = connectDB4.createStatement();
                ResultSet query = statement.executeQuery(queryoutput);
                while (query.next()) {
                    String firstname_children_column = query.getString("first_name");
                    String lastname_children_column = query.getString("last_name");
                    String date_of_birth = query.getString("date_of_birth");
                    ParentsSearchModel childobject= new ParentsSearchModel(firstname_children_column,lastname_children_column,date_of_birth);
                    childrenDesplay.add(childobject.toString());

                }

            }
            catch (SQLException e) {
                Logger.getLogger(ShowParentsController.class.getName()).log(Level.SEVERE, null, e);
            }
            String textToDisplay;
            StringBuilder sb = new StringBuilder();
            for(String s: childrenDesplay){
                sb.append(s);

            }


            screen.setText(sb.toString());
            screen.setStyle("-fx-text-fill: #0f123f;");
            screen.setStyle("-fx-background-color: #fff;");
            screen.setStyle("-fx-background-radius: 10;");
            screen.setStyle("-fx-border-color: #0f123f;");
            screen.setStyle("-fx-border-radius: 10;");
            screen.setStyle("-fx-border-width: 2;");
            screen.setStyle("-fx-font-size: 16;");
            screen.setStyle("-fx-font-weight: bold;");

            //make it visible everytime is clicked and desappear when clicked again
            screen.setVisible(true);

            //background of the text
            screen.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,1), CornerRadii.EMPTY, Insets.EMPTY)));


        }
        else{
            screen.setText("");
        }
    }




















        @FXML
        private void print (ActionEvent event){
            //print the tableview and the screen

            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);


            //get printer text responsive
            //include the screen on tableview
            double scaleX = pageLayout.getPrintableWidth() / parents_tableview.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / parents_tableview.getBoundsInParent().getHeight();
            double scaleX1= pageLayout.getPrintableWidth() / screen.getBoundsInParent().getWidth();
            double scaleY1= pageLayout.getPrintableHeight() / screen.getBoundsInParent().getHeight();




            scaleX = scaleX > 1 ? 1 : scaleX;
            scaleY = scaleY > 1 ? 1 : scaleY;
            scaleX1 = scaleX1 > 1 ? 1 : scaleX1;
            scaleY1 = scaleY1 > 1 ? 1 : scaleY1;
            //open logo on the top of the tableview
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream("src/main/resources/com/example/ui/images/login.png");

            }
            catch (FileNotFoundException e) {
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
            parents_tableview.getTransforms().add(new Scale(scaleX, scaleY));
            screen.getTransforms().add(new Scale(scaleX1, scaleY1));



            //print the tableview
            PrinterJob job = PrinterJob.createPrinterJob();

            if (job != null) {
                boolean printed = job.printPage(pageLayout, parents_tableview);
                boolean printed1 = job.printPage(pageLayout, screen);
                if (printed && printed1) {
                    job.endJob();
                }
            }
        }


    }


