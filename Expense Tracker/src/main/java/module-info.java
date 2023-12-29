module com.example.expensetracker {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.expensetracker to javafx.fxml;
    exports com.example.expensetracker;
    exports com.example.expensetracker.controllers;
    opens com.example.expensetracker.controllers to javafx.fxml;
}