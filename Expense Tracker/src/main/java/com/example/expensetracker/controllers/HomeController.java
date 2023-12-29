package com.example.expensetracker.controllers;

import com.example.expensetracker.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    private Stage primaryStage;
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }
    public void redirectSignup(){
        try{
            FXMLLoader signupLoader = new FXMLLoader(Main.class.getResource("Signup.fxml"));
            Scene scene = new Scene(signupLoader.load(), 1280, 700);

            SignupController signupController = signupLoader.getController();
            signupController.setPrimaryStage(this.primaryStage);
            primaryStage.setTitle("Signup");
            primaryStage.setScene(scene);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void redirectLogin(){
        try{
            FXMLLoader loginLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
            Scene scene = new Scene(loginLoader.load(), 1280, 700);
            LoginController loginController = loginLoader.getController();
            loginController.setPrimaryStage(this.primaryStage);
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
