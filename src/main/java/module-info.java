module com.example.roskildedaycareproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.ui to javafx.fxml;
    exports com.example.ui;
}