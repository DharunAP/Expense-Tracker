package com.example.expensetracker.controllers;

import com.example.expensetracker.DatabaseConnection;
import com.example.expensetracker.Main;
import com.example.expensetracker.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupController {

    @FXML
    private TextField groupname;
    @FXML
    private TextArea desc;
    @FXML
    private Button split;
    @FXML
    private TextField amount;
    @FXML
    private ChoiceBox<String> choiceList;
    @FXML
    private TextField participantUserID;
    @FXML
    private TextField participantAmount;
    @FXML
    private AnchorPane anchor;
    @FXML
    private TableView<Participant> tableParticipants;
    @FXML
    private TableColumn<Participant,String> columnParticipants;
    @FXML
    private TableColumn<Participant,String> columnAmount;


    public void init() {
        choiceList.getItems().addAll(getAllUsers());
    }
    public ArrayList<String> getAllUsers(){
        ArrayList<String> users = new ArrayList<>();
        String userId = userDetails.get("userid");
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnection();
            Statement st  = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT User_Name from user where user_id <> "+userId);
            while(rs.next()){
                users.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    private Stage primaryStage;
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    public void redirectBack(){
        try{
            FXMLLoader dashLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
            Scene scene = new Scene(dashLoader.load(), 1280, 700);

            DashboardController dashController = dashLoader.getController();
            dashController.setPrimaryStage(this.primaryStage);
            dashController.setDetails(userDetails);
            primaryStage.setTitle("Dashboard");
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ArrayList<String> participantarr = new ArrayList<>();
    ArrayList<String> amountarr = new ArrayList<>();


//    public void split_equally() throws Exception {
//        int total_amount = Integer.parseInt(amount.getText());
//        int splitted_amount = total_amount/participantarr.size();
//        System.out.println(String.valueOf(splitted_amount));
//        System.out.println(participantarr);
//        for(String element: participantarr){
//            Splitamountarr.add(String.valueOf(splitted_amount));
//        }
//        split.setVisible(false);
//        populateAmount(Splitamountarr);
//
//    }
    int i = 0;
    public void populateArray() throws Exception {
            amountarr.add(i, participantAmount.getText());
            participantarr.add(i,choiceList.getValue());
            System.out.println(amountarr);
            System.out.println(participantarr);
            populateParticipants();
            i++;
        }
    private HashMap<String, String> userDetails;
    public void setDetails(HashMap<String, String> k ){
        this.userDetails = k;
    }

    public int getSender(String userName){
        try{
            if(userName.isEmpty()) return 0;
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT User_ID from user where User_Name = \""+userName+"\"");
            rs.next();
            return rs.getInt(1);
        }catch(Exception e){
            return 0;
        }
    }
    public ArrayList<Integer> generateArray(ArrayList<String> n){
        ArrayList<Integer> nn = new ArrayList<>();
        for(String name: n){
            nn.add(getSender(name));
        }
        return nn;
    }
    public ArrayList<Integer> convertAmount(ArrayList<String> n){
        ArrayList<Integer> nn = new ArrayList<>();
        for(String name: n){
            nn.add(Integer.parseInt(String.valueOf(name)));
        }
        return nn;
    }
    public void createNewGroup(){
        createGroup(groupname.getText(),Integer.parseInt(userDetails.get("userid")),desc.getText(),Integer.parseInt(amount.getText()),generateArray(participantarr),convertAmount(amountarr));
    }
    public static void createGroup(String name, int creator_ID, String description, int total_amount, ArrayList<Integer> members, ArrayList<Integer> amount){
        try{
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement("Insert into GroupOfUsers(Name, Created_By, Description, Total_Amount) values(?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, String.valueOf(creator_ID));
            ps.setString(3, description);
            ps.setString(4, String.valueOf(total_amount));
            ps.executeUpdate();

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT Group_ID from GroupOfUsers where description=\""+description+"\" and created_by="+creator_ID+" and total_amount="+total_amount);
            rs.next();
            int grpId = rs.getInt(1);

            for(int i=0;i<members.size();i++){
                System.out.println(members.get(i));
                ps = con.prepareStatement("Insert into GroupJoin(Group_Id, user_Id, Amount, OwesTo) values(?,?,?,?)");
                ps.setInt(1, grpId);
                ps.setString(2, String.valueOf(members.get(i)));
                ps.setString(3, String.valueOf(amount.get(i)));
                ps.setString(4, String.valueOf(creator_ID));
                ps.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void populateParticipants() throws Exception{
        ObservableList<Participant> AllParticipants = FXCollections.observableArrayList();

        try{
            int i = 0;
            for (String part: participantarr) {
                Participant one_expense = new Participant();

                System.out.println(part);
                one_expense.setParticipant(part);
                one_expense.setAmount(amountarr.get(i));
                AllParticipants.add(one_expense);
                i++;
            }

            tableParticipants.setItems(AllParticipants);
            columnParticipants.setCellValueFactory(f -> f.getValue().participantProperty());
            columnAmount.setCellValueFactory(f -> f.getValue().amountProperty());

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }


    }

}
