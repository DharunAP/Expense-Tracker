package com.example.expensetracker.controllers;

import com.example.expensetracker.DatabaseConnection;
import com.example.expensetracker.Main;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

import static java.time.Duration.*;


public class SignupController {
    @FXML
    private ImageView tick;
    @FXML
    private ImageView cross;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField rePassword;
    @FXML
    private TextField monthlyIncome;
    @FXML
    private Label labelMessage;
    @FXML
    private Stage primaryStage;
    public void showSuccess() {
        labelMessage.setText("Registration successful!");
        labelMessage.setVisible(true);
        labelMessage.setTextFill(Color.web("#009a12"));
        tick.setVisible(true);
        // Call the method to animate the label
        animateLabel();
        animateSymbol(tick);
    }
    public void showFailure(String error){
        labelMessage.setText(error);
        labelMessage.setTextFill(Color.rgb(255, 0, 0));
        labelMessage.setVisible(true);
        cross.setVisible(true);
        animateLabel();
        animateSymbol(cross);
    }
    private void animateLabel() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), labelMessage);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        // Uncomment the line below if you want to hide the label after animation
         fadeTransition.setOnFinished(event -> labelMessage.setVisible(false));

        fadeTransition.play();
    }
    private void animateSymbol(ImageView symbol) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), symbol);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        // Uncomment the line below if you want to hide the label after animation
        fadeTransition.setOnFinished(event -> symbol.setVisible(false));

        fadeTransition.play();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void redirectBack(){
        try{
            FXMLLoader homeLoader = new FXMLLoader(Main.class.getResource("Home.fxml"));
            Scene scene = new Scene(homeLoader.load(), 1280, 700);

            HomeController homeController = homeLoader.getController();
            homeController.setPrimaryStage(this.primaryStage);
            primaryStage.setTitle("Home");
            primaryStage.setScene(scene);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    public boolean userAlreadyExists(String userName){
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT User_ID from user where User_Name=\""+userName+"\"");
            return rs.next();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
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
    public void createUser(){
        String userName = username.getText();
        String  rPassword = rePassword.getText();
        String Password = password.getText();
        String Income = monthlyIncome.getText();
        try{
            if(userAlreadyExists(userName)){
                throw new Exception("  Username already taken!");
            }
            if (userName.isEmpty() || Password.isEmpty() || Income.isEmpty() ){
                throw new Exception("  Fill all the fields!");
            }
            if (!(Password.equals(rPassword))){
                throw new Exception("  Confirm password mismatch!");
            }
            if (userName.length()<5){
                throw new Exception("  Username too short!");
            }
            if(Password.length()<4){
                throw new Exception("  Password must contain >4 chars");
            }
            try {
                Integer.parseInt(Income);
            } catch (Exception e) {
                throw new Exception("  Income must be integer!");
            }
            String username = "griffin";
            String password = "root";
            String database_name = "javafx";
            String url = "jdbc:mysql://localhost/" + database_name;
            Connection con = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = con.prepareStatement("Insert into user(User_Name, Password, Balence_Amount, Income) values(?,?,?,?)");
            ps.setString(1, userName);
            ps.setString(2, Password);
            ps.setString(3, String.valueOf(Income));
            ps.setString(4, String.valueOf(Income));
            ps.executeUpdate();
            showSuccess();
            redirectLogin();
        }catch(Exception e){
            showFailure(e.getMessage());
            e.printStackTrace();
        }
    }
}
