package com.example.expensetracker;
import com.example.expensetracker.controllers.HomeController;
import com.example.expensetracker.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader homeLoader = new FXMLLoader(Main.class.getResource("Home.fxml"));
        Scene scene = new Scene(homeLoader.load(), 1280, 700);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();

        HomeController homeController = homeLoader.getController();
        homeController.setPrimaryStage(stage);

    }
    public static void main(String[] args) {
        launch();
    }
}


//    FXMLLoader loginLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
//    Scene scene = new Scene(loginLoader.load(), 600, 400);
//        stage.setTitle("Login");
//                stage.setScene(scene);
//                stage.show();
//
//                LoginController loginController = loginLoader.getController();
//                loginController.setPrimaryStage(stage);
//LoginController loginController = loginLoader.getController();
//        loginController.setPrimaryStage(stage);