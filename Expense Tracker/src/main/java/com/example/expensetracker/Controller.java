package com.example.expensetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
     private Label showUserDetails;
     @FXML
     private Label showUserID;



     public void connectButton(ActionEvent event){
         System.out.println("button");

      DatabaseConnection connection = new DatabaseConnection();
      Connection conn = connection.getConnection();

      String getQuery = "Select * from users";

         try {
             Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(getQuery);

             while(result.next()){
                 showUserID.setText(result.getString("user_id"));
                 showUserDetails.setText(result.getString("username"));
             }

         } catch (SQLException e) {
             throw new RuntimeException(e);
         }

     }

}