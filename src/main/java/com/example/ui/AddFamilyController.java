package com.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddFamilyController implements Initializable {

    @FXML
    private TextField child_first_name_textfield;
    @FXML
    private TextField child_last_name_textfield;
    @FXML
    private DatePicker child_date_of_birth_datepicker;
    @FXML
    private TextField parent_1_first_name_textfield;
    @FXML
    private TextField parent_1_last_name_textfield;
    @FXML
    private TextField parent_1_phone_number_textfield;
    @FXML
    private TextField parent_2_first_name_textfield;
    @FXML
    private TextField parent_2_last_name_textfield;
    @FXML
    private TextField parent_2_phone_number_textfield;
    @FXML
    private Button cancel_button;

    private boolean go = true;
    private boolean parent1_exists;
    private boolean parent2_exists;
    private PreparedStatement insert;
    private PreparedStatement check_database;
    private ResultSet rs = null;

    private Integer childid = null;
    private Integer parent1id = null;
    private Integer parent2id = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        cancel_button.setOnAction(actionEvent -> stage.close());
    }

    public static void AddFamily() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AddFamilyController.class.getResource("add_family.fxml"));
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            Stage add = new Stage();
            add.setTitle("Add Family");
            add.setScene(scene);
            add.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(AddFamilyController.class.getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }


    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage add = (Stage) cancel_button.getScene().getWindow();
        add.close();
    }

    @FXML
    public void add_to_db_action(ActionEvent event) {
        //Create Connection
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();
        go = true;

        //Check if all Textboxes are filled
        if (go) {
            if (child_first_name_textfield.getText().trim().isEmpty()
                    || child_last_name_textfield.getText().trim().isEmpty()
                    || parent_1_first_name_textfield.getText().trim().isEmpty()
                    || parent_1_last_name_textfield.getText().trim().isEmpty()
                    || parent_1_phone_number_textfield.getText().trim().isEmpty()
                    || parent_2_first_name_textfield.getText().trim().isEmpty()
                    || parent_2_last_name_textfield.getText().trim().isEmpty()
                    || parent_2_phone_number_textfield.getText().trim().isEmpty()
                    || child_date_of_birth_datepicker.getValue() == null) {
                System.out.println(child_date_of_birth_datepicker.getValue());
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill all info!");
                alert.show();
                go = false;
            }
        }

        try {
            //Check if Child exist already
            if (go) {
                check_database = connectDB.prepareStatement("SELECT first_name,last_name,date_of_birth FROM children WHERE date_of_birth = ? AND first_name = ? AND last_name = ?");
                check_database.setString(1, child_date_of_birth_datepicker.getValue().toString());
                check_database.setString(2, child_first_name_textfield.getText());
                check_database.setString(3, child_last_name_textfield.getText());
                rs = check_database.executeQuery();
                if (rs.isBeforeFirst()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Child already exists");
                    alert.show();
                    go = false;
                }
            }

            //Check if Parent 1 exist already/store parentID
            if (go) {
                check_database = connectDB.prepareStatement("SELECT first_name,last_name,phone FROM parents WHERE phone = ? AND first_name = ? AND last_name = ?");
                check_database.setString(1, parent_1_phone_number_textfield.getText());
                check_database.setString(2, parent_1_first_name_textfield.getText());
                check_database.setString(3, parent_1_last_name_textfield.getText());
                rs = check_database.executeQuery();
                //Insert Parent 1 into DB
                if (rs.isBeforeFirst()) {
                    if (confirm("Do you want to use this info for parent 1?\n" +
                            "Parent 1 Name: " + parent_1_first_name_textfield.getText() + "\n" +
                            "Parent 1 LastName: " + parent_1_last_name_textfield.getText() + "\n" +
                            "Parent 1 Phone No: " + parent_1_phone_number_textfield.getText(), "Confirm existing parent")) {
                        //Retrieve ParentID if confirmation
                        parent1_exists = true;
                        check_database = connectDB.prepareStatement("SELECT parents.parent_id from relations RIGHT JOIN children c on relations.child_id = c.child_id RIGHT JOIN parents on relations.parent_id = parents.parent_id WHERE daycare.parents.first_name = ? AND daycare.parents.last_name = ? and daycare.parents.phone = ?;");
                        check_database.setString(1, parent_1_first_name_textfield.getText());
                        check_database.setString(2, parent_1_last_name_textfield.getText());
                        check_database.setString(3, parent_1_phone_number_textfield.getText());
                        rs = check_database.executeQuery();
                        while (rs.next()) {
                            parent1id = rs.getInt("parent_id");
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Input Parent 1 informations again", ButtonType.CLOSE);
                        alert.show();
                        go = false;
                    }
                }
            }

            //Check if Parent 2 exist already/Store ParentID

            if (go) {
                check_database = connectDB.prepareStatement("SELECT parents.parent_id from relations RIGHT JOIN children c on relations.child_id = c.child_id RIGHT JOIN parents on relations.parent_id = parents.parent_id WHERE daycare.parents.first_name = ? AND daycare.parents.last_name = ? and daycare.parents.phone = ?;");
                check_database.setString(1, parent_2_first_name_textfield.getText());
                check_database.setString(2, parent_2_last_name_textfield.getText());
                check_database.setString(3, parent_2_phone_number_textfield.getText());
                rs = check_database.executeQuery();
                //Insert Parent 2 into DB
                if (rs.isBeforeFirst()) {
                    if (confirm("Do you want to use this info for parent 2?: \n" +
                            "Parent 2 Name: " + parent_2_first_name_textfield.getText() + "\n" +
                            "Parent 2 LastName: " + parent_2_last_name_textfield.getText() + "\n" +
                            "Parent 2 Phone No: " + parent_2_phone_number_textfield.getText(), "Confirm existing parent")) {
                        //Retrieve ParentID 2
                        parent2_exists = true;
                        check_database = connectDB.prepareStatement("SELECT parents.parent_id from relations RIGHT JOIN children c on relations.child_id = c.child_id RIGHT JOIN parents on relations.parent_id = parents.parent_id WHERE daycare.parents.first_name = ? AND daycare.parents.last_name = ? and daycare.parents.phone = ?;");
                        check_database.setString(1, parent_2_first_name_textfield.getText());
                        check_database.setString(2, parent_2_last_name_textfield.getText());
                        check_database.setString(3, parent_2_phone_number_textfield.getText());
                        rs = check_database.executeQuery();
                        while (rs.next()) {
                            parent2id = rs.getInt("parent_id");
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Input Parent 2 informations again", ButtonType.CLOSE);
                        alert.show();
                        go = false;
                    }
                }
            }
            //System.out.println("parent 2 id =" + parent2id);

            //Alert what info will be inserted
            if (go) {
                if (confirm("These will be added to the Database:\n" +
                        "Child Name: " + child_first_name_textfield.getText() + "\n" +
                        "Child Lastname: " + child_last_name_textfield.getText() + "\n" +
                        "Child Date of Birth: " + child_date_of_birth_datepicker.getValue().toString() + "\n" +
                        "Parent 1 Name: " + parent_1_first_name_textfield.getText() + "\n" +
                        "Parent 1 LastName: " + parent_1_last_name_textfield.getText() + "\n" +
                        "Parent 1 Phone No: " + parent_1_phone_number_textfield.getText() + "\n" +
                        "Parent 2 Name: " + parent_2_first_name_textfield.getText() + "\n" +
                        "Parent 2 LastName: " + parent_2_last_name_textfield.getText() + "\n" +
                        "Parent 2 Phone No: " + parent_2_phone_number_textfield.getText(), "Info to be Added")) {
                } else {
                    go = false;
                }
            }

            //Insert Child/Parents into DB
            if (go) {
                //Insert Child into DB
                PreparedStatement insert = connectDB.prepareStatement("INSERT INTO daycare.children(first_name, last_name, date_of_birth) VALUE (?,?,?);");
                insert.setString(1, child_first_name_textfield.getText());
                insert.setString(2, child_last_name_textfield.getText());
                insert.setString(3, child_date_of_birth_datepicker.getValue().toString());
                insert.executeUpdate();
                //System.out.println("Added child: " + child_first_name_textfield.getText() + ", " + child_last_name_textfield.getText() + ", " + child_date_of_birth_datepicker.getValue().toString());
                //Insert Parent1 into DB
                if (!parent1_exists) {
                    insert = connectDB.prepareStatement("INSERT INTO daycare.parents(first_name, last_name, phone) VALUE (?,?,?);");
                    insert.setString(1, parent_1_first_name_textfield.getText());
                    insert.setString(2, parent_1_last_name_textfield.getText());
                    insert.setString(3, parent_1_phone_number_textfield.getText());
                    insert.executeUpdate();
                    System.out.println("Parent 1 added: " + parent_1_first_name_textfield.getText() + " " + parent_1_last_name_textfield.getText() + " " + parent_1_phone_number_textfield.getText());
                }
                //Insert Parent 2 into DB
                if (!parent2_exists) {
                    insert = connectDB.prepareStatement("INSERT INTO parents(first_name, last_name, phone) VALUE (?,?,?);");
                    insert.setString(1, parent_2_first_name_textfield.getText());
                    insert.setString(2, parent_2_last_name_textfield.getText());
                    insert.setString(3, parent_2_phone_number_textfield.getText());
                    insert.executeUpdate();
                    System.out.println("Parent 1 added: " + parent_2_first_name_textfield.getText() + " " + parent_2_last_name_textfield.getText() + " " + parent_2_phone_number_textfield.getText());
                }
            }

            //Check/store childID
            if (go) {
                try{
                check_database = connectDB.prepareStatement("SELECT child_id from children order by child_id desc limit 1");
                rs = check_database.executeQuery();
                while (rs.next()) {
                    childid = rs.getInt("child_id");
                    //System.out.println("child id =" + childid);
                }}
                catch (SQLException e){
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Shit happened");
                    alert.show();
                }
            }

            //Check/Store parentsIDs
            if (go) {
                if (parent1id == null) {
                    check_database = connectDB.prepareStatement("SELECT parents.parent_id from parents WHERE daycare.parents.first_name = ? AND daycare.parents.last_name = ? and daycare.parents.phone = ?;");
                    check_database.setString(1, parent_1_first_name_textfield.getText());
                    check_database.setString(2, parent_1_last_name_textfield.getText());
                    check_database.setString(3, parent_1_phone_number_textfield.getText());
                    rs = check_database.executeQuery();
                    while (rs.next()) {
                        parent1id = rs.getInt("parent_id");
                        //System.out.println("parent 1 id =" + parent1id);
                    }
                }
                if (parent2id == null) {
                    check_database = connectDB.prepareStatement("SELECT parents.parent_id from parents WHERE daycare.parents.first_name = ? AND daycare.parents.last_name = ? and daycare.parents.phone = ?;");
                    check_database.setString(1, parent_2_first_name_textfield.getText());
                    check_database.setString(2, parent_2_last_name_textfield.getText());
                    check_database.setString(3, parent_2_phone_number_textfield.getText());
                    rs = check_database.executeQuery();
                    while (rs.next()) {
                        parent2id = rs.getInt("parent_id");
                        //System.out.println("parent 2 id =" + parent2id);
                    }
                }
            }

            //Insert relation
            if (go) {
                insert = connectDB.prepareStatement("INSERT INTO relations(parent_id, child_id) VALUE (?,?);");
                insert.setString(1, parent1id.toString());
                insert.setString(2, childid.toString());
                insert.executeUpdate();

                insert = connectDB.prepareStatement("INSERT INTO relations(parent_id, child_id) VALUE (?,?);");
                insert.setString(1, parent2id.toString());
                insert.setString(2, childid.toString());
                insert.executeUpdate();
            }

            //Message Child inserted
            if (go) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Added to Database", ButtonType.CLOSE);
            alert.show();}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean confirm(String message, String title) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.NO, ButtonType.YES);
        confirmation.setTitle(title);
        Optional<ButtonType> action = confirmation.showAndWait();
        if (action.isPresent() && action.get() == ButtonType.YES) {
            return true;
        }
        return false;
    }


}

