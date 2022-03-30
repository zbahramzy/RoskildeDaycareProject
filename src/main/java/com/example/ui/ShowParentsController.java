package com.example.ui;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.print.*;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.xml.transform.Result;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    @FXML
    private PieChart piechart;
    @FXML
            private Label caption;


    ObservableList<ParentsSearchModel> parentsSearchModelObservableList = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();


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
        //run sql query to get all parents count from the database
        

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
                    if (parentsSearchModel.getFirst_name().toLowerCase().contains(searchkeyword)) {
                        return true;
                    } else if (parentsSearchModel.getLast_name().toLowerCase().contains(searchkeyword)) {
                        return true;
                    } else if (parentsSearchModel.getPhone().toLowerCase().contains(searchkeyword)) {
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

//pie chart handeler

        String sql_piechart = "SELECT count(enrollments.child_id) as m1   from enrollments   union all\n" +
                "select count(waiting_list.waiting_order_id) as m2  from waiting_list;";
        try {
            Statement stmt = connectDB.createStatement();
            ResultSet rs_pie = stmt.executeQuery(sql_piechart);
            int i1= 0;
            int i2= 0;
            int enrolments_count=0;
            int waiting_list_count=0;
            ArrayList<Integer>numbersPie=new ArrayList<>();
            while (rs_pie.next()) {
                numbersPie.add(rs_pie.getInt("m1"));

            }
            enrolments_count=numbersPie.get(0);
            waiting_list_count=numbersPie.get(1);
            //get persentage of enrolments_count and waiting_list_count
            double enrolments_percentage=(enrolments_count*100)/(enrolments_count+waiting_list_count);
            double waiting_list_percentage=(waiting_list_count*100)/(enrolments_count+waiting_list_count);


            //make a pie chart show

            pieChartData.add(new PieChart.Data("Waiting List", waiting_list_percentage));
            pieChartData.add(new PieChart.Data("Enrolled ",enrolments_percentage));
            piechart.setData(pieChartData);



            //piechart data number show




            piechart.setStartAngle(90);
            piechart.setClockwise(true);
            piechart.setVisible(true);
            //piechart fancy looks make it with details

            piechart.setLabelLineLength(20);
            piechart.setLegendSide(Side.BOTTOM);
            //Processing Events for a Pie Chart
            piechart.setTitle("Enrollment Status");
            piechart.setLegendVisible(true);
            piechart.setLabelsVisible(true);
            //make pie chart animated with animation with duration
            piechart.getData().forEach(data -> {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                    data.getNode().setEffect(i1 == 0 ? new DropShadow() : null);


                    //make it to rotate when mouse pressed
                    data.getNode().setOnMousePressed(e1 -> {
                        RotateTransition rt = new RotateTransition(Duration.millis(1000), data.getNode());

                        rt.setByAngle(360);

                        rt.play();
                    });

                    //make sure to rotate when mouse released




                    FadeTransition ft = new FadeTransition(Duration.millis(1000), data.getNode());
                    ft.setFromValue(0.1);
                    ft.setToValue(1);
                    ft.play();

                }
                );

                    }
            );
            //make it to rotate when mouse pressed




            caption.setTextFill(Color.web("#0f123f"));
            caption.setStyle("-fx-font: 20 arial;");
            for (final PieChart.Data data : piechart.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                System.out.println("Pie Chart Data: " + data.getName()+" "+data.getPieValue());
                                caption.setTranslateX(e.getSceneX()- caption.getLayoutX());
                                caption.setTranslateY(e.getSceneY()- caption.getLayoutY());
                                caption.setText(String.valueOf(data.getPieValue()) + "%");




                            }
                        });
            }

            //make a pichart animated
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(5000), piechart);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();



        }
        catch (SQLException e) {
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
//make a parents_table work with vbox and animated transition and fade in







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
            String queryoutput = " SELECT children.first_name, children.last_name, children.date_of_birth from daycare.children\n" +
                    "                       left JOIN daycare.relations r on children.child_id = r.child_id\n" +
                    "                        JOIN  daycare.parents on r.parent_id = parents.parent_id \n" +
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
            //set style as animation for the text
            FadeTransition ft = new FadeTransition(Duration.millis(2000), screen);

            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            //make a loop to show the text for 3 seconds


            ft.play();




            //make it visible everytime is clicked and desappear when clicked again
            screen.setVisible(true);

            // hide the text after 5 seconds
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            screen.setVisible(false);

                        }
                    }
                    , 10000);













        }
        else{
            //make screen color transparent
            screen.setStyle("-fx-background-color: transparent;");
            screen.setText("");
            screen.clear();
            screen.setVisible(false);


            //chanege background to #0f123f in e
        }
        //make translate transition for the vbox

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
            double scaleX3= pageLayout.getPrintableWidth() / piechart.getBoundsInParent().getWidth();
            double scaleY3= pageLayout.getPrintableHeight() / piechart.getBoundsInParent().getHeight();





            scaleX = scaleX > 1 ? 1 : scaleX;
            scaleY = scaleY > 1 ? 1 : scaleY;
            scaleX1 = scaleX1 > 1 ? 1 : scaleX1;
            scaleY1 = scaleY1 > 1 ? 1 : scaleY1;
            scaleX3 = scaleX3 > 1 ? 1 : scaleX3;
            scaleY3 = scaleY3 > 1 ? 1 : scaleY3;
            //open logo on the top of the tableview









            //make the table looks good on print
            //show the image on the top of the tableview
            parents_tableview.getTransforms().add(new Scale(scaleX, scaleY));
            screen.getTransforms().add(new Scale(scaleX1, scaleY1));
            screen.setVisible(true);
            piechart.getTransforms().add(new Scale(scaleX3, scaleY3));
            piechart.setVisible(true);
            //set png image on the top of the print
            //get the screen on the top of the print page





            //print the tableview and the screen and piechart in different pages

            PrinterJob job = PrinterJob.createPrinterJob();

            if (job != null) {
                boolean printed = job.printPage(pageLayout, parents_tableview);
                boolean printed1 = job.printPage(pageLayout, screen);
                boolean printed2 = job.printPage(pageLayout, piechart);
                if (printed && printed1 && printed2) {
                    job.endJob();
                }
            }
        }


    }


