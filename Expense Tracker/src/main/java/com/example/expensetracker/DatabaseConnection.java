package com.example.expensetracker;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseConnection {

    public Connection connect_to_database;
    public Connection getConnection(){
        String username = "griffin";
        String password = "root";
        String database_name = "javafx";
        String url = "jdbc:mysql://localhost/" + database_name;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect_to_database = DriverManager.getConnection(url,username,password);
        }
        catch(Exception e){
            System.out.println("Error in connecting to database");
            e.printStackTrace();
        }

        return connect_to_database;
    }
}
