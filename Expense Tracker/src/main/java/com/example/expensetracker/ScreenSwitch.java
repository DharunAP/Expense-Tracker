package com.example.expensetracker;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class ScreenSwitch {
    public ScreenSwitch(AnchorPane currentAnchorPane,String a) throws IOException{
        AnchorPane nextAnchorPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(a)));
        currentAnchorPane.getChildren().removeAll();
        currentAnchorPane.getChildren().setAll(nextAnchorPane);
    }
}
