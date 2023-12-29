package com.example.expensetracker.controllers;
import com.example.expensetracker.DatabaseConnection;
import com.example.expensetracker.Main;
import com.mysql.cj.jdbc.result.UpdatableResultSet;
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
import java.util.HashMap;

public class LoginController {
    private Stage primaryStage;
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;
    @FXML
    private ImageView tick;
    @FXML
    private ImageView cross;
    @FXML
    private Label labelMessage;

    public void showSuccess() {
        labelMessage.setText("  Login Successful!");
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
    private int user_id;
    public void setUser_id(int userid){
        this.user_id = userid;
    }
    public int getUser_id(){
        return this.user_id;
    }
    public void Login(){
        String userName = username.getText();
        String Password = password.getText();
        int id =VerifyUser(userName,Password);
        if( id != 0) {
            loginUser(userName,Password);
            setUser_id(id);
            showSuccess();
            redirectDashboard();
        }else{
        showFailure("  Invalid Credentials!");
        }

    }
    public int VerifyUser(String userName,String Password){

            int id =0;
            try{
                String username = "griffin";
                String password = "root";
                String database_name = "javafx";
                String url = "jdbc:mysql://localhost/" + database_name;
                Connection con = DriverManager.getConnection(url,username,password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT User_ID, Password from user where User_Name=\""+userName+"\"");
                rs.next();
                String pass = rs.getString("Password");
                System.out.println(pass);
                if((Password.equals(pass))){
                    id = Integer.parseInt(rs.getString("User_ID"));
                    return id;
                }
                else{
                    return 0;
                }
            }catch(Exception e){
                e.printStackTrace();
                return 0;
            }
        }


        public HashMap<String, String> getUser(int id){
            HashMap<String, String> hs = new HashMap<>();
            try{
                String username = "griffin";
                String password = "root";
                String database_name = "javafx";
                String url = "jdbc:mysql://localhost/" + database_name;
                Connection con = DriverManager.getConnection(url,username,password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT User_ID,User_Name, Balence_Amount, Income from user where User_ID = "+id);
                rs.next();
                hs.put("userid",rs.getString("User_ID"));
                hs.put("UserName",rs.getString("User_Name"));
                // hs.put("Password",rs.getString("Password"));
                hs.put("Balance",rs.getString("Balence_Amount"));
                hs.put("Income",rs.getString("Income"));
                System.out.println(hs.get("UserName")+" retrived successfully...");
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
            return hs;
        }


    public void redirectDashboard(){
        try{
            FXMLLoader dashLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
            Scene scene = new Scene(dashLoader.load(), 1280, 700);

            DashboardController dashController = dashLoader.getController();
            dashController.setPrimaryStage(this.primaryStage);
            dashController.setDetails(getUser(getUser_id()));
            dashController.init();
            primaryStage.setTitle("Dashboard");
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static int loginUser(String Use, String Password){
        int id =0;
        try{
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT User_ID, Password from user where User_Name=\""+Use+"\"");
            rs.next();
            String pas = rs.getString("Password");
            String User_ID = rs.getString(1);
            System.out.println(pas);
            if(!(Password==pas)) id = Integer.parseInt(rs.getString("User_ID"));
            st.executeUpdate("Truncate table CurrUser");
            PreparedStatement ps = con.prepareStatement("Insert into CurrUser values(?,?)");
            ps.setString(1, User_ID);
            ps.setString(2, Use);
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
        return id;
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

}
