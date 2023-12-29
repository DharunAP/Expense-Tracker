package com.example.expensetracker.controllers;

import com.example.expensetracker.DatabaseConnection;
import com.example.expensetracker.Main;
import com.example.expensetracker.ScreenSwitch;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DashboardController {
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    @FXML
    private ImageView tick;
    @FXML
    private Label labelMessage;
    @FXML
    private ImageView cross;
    @FXML
    private Label name;
    @FXML
    private Label yearly;
    @FXML
    private Label monthly;
    @FXML
    private Label weekly;
    @FXML
    private Label income;
    @FXML
    private Label balance;
    @FXML
    private TableColumn<Group, String> newgroup;
    @FXML
    private ChoiceBox<String> receiverChoice;


    @FXML
    private TableView<Group> tableGroup;
    @FXML
    private TableView<UserExpenses> expenseTable;
    @FXML
    private TableColumn<UserExpenses, Integer> ColumnAmount;
    @FXML
    private TableColumn<UserExpenses, String> ColumnDescription;
    @FXML
    private TableColumn<UserExpenses, String> ColumnType;
    @FXML
    private TableColumn<UserExpenses, String> ColumnReceiver;
    @FXML
    private TableColumn<UserExpenses, String> ColumnCategory;
    @FXML
    private TableColumn<UserExpenses, String> ColumnDate;

    @FXML
    private TextField amount;
    @FXML
    private TextField description;
    @FXML
    private MenuButton cateParent;
    @FXML
    public void setFood() throws Exception{
        cateParent.setText("food");
    }
    @FXML
    public void setGrocery() throws Exception{
        cateParent.setText("Grocery");
    }
    @FXML
    public void setShopping() throws Exception{
        cateParent.setText("Shopping");
    }
    @FXML
    public void setSchool() throws Exception{
        cateParent.setText("School");
    }
    @FXML
    public void setOther() throws Exception{
        cateParent.setText("Other");
    }
    @FXML
    private AnchorPane a2;

    @FXML
    void tos1(ActionEvent event) throws IOException {
        new ScreenSwitch(a2,"AddGroup.fxml");

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
    public void init() {
        receiverChoice.getItems().addAll(getAllUsers());
    }
    public void show(){
        a2.setDisable(false);
    }
    private HashMap<String,String> userDetails;
    public void setUserDetails(HashMap<String,String> k){
        this.userDetails = k;
    }
    public static ArrayList<Integer> getExpenditure(int user_Id){
        ArrayList<Integer> s =new ArrayList<>();
        try{
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount) as amt from transactions where sender = "+user_Id+" and type = \"Expenditure\" and year(date)= year(curdate())");
            if(rs.next())
                s.add(rs.getInt(1));
            else
                s.add(0);

            rs = st.executeQuery("select sum(amount) as amt from transactions where sender = "+user_Id+" and type = \"Expenditure\" and month(date)= month(curdate())");
            if(rs.next())
                s.add(rs.getInt(1));
            else
                s.add(0);

            rs = st.executeQuery("select sum(amount) as amt from transactions where sender = "+user_Id+" and type = \"Expenditure\" and date= curdate()");
            if(rs.next())
                s.add(rs.getInt(1));
            else
                s.add(0);
            return s;
        }catch(Exception e){
            e.printStackTrace();
        }
        return s;
    }
    public static int getBalence(int userID){
        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnection();
            Statement st  = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT BALENCE_AMOUNT FROM USER where user_ID = "+userID);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }
    public void setBalance(String bal){
        balance.setText(bal);
    }
    public void setExpenditure(HashMap<String,String> k){
        ArrayList<Integer> nn =  getExpenditure(Integer.parseInt(k.get("userid")));
        System.out.println(nn);
        yearly.setText(String.valueOf(nn.get(0)));
        monthly.setText(String.valueOf(nn.get(1)));
        weekly.setText(String.valueOf(nn.get(2)));
    }
    public void setDetails(HashMap<String, String> k) throws SQLException {
        setUserDetails(k);
        populateTable(k);
        table();
        System.out.println(Integer.parseInt(k.get("userid")));
        ArrayList<Integer> nn =  getExpenditure(Integer.parseInt(k.get("userid")));
        System.out.println(nn);
        yearly.setText(String.valueOf(nn.get(0)));
        monthly.setText(String.valueOf(nn.get(1)));
        weekly.setText(String.valueOf(nn.get(2)));
        name.setText("Welcome " + k.get("UserName") + "!");
        income.setText(k.get("Income"));
        balance.setText(k.get("Balance"));
    }

    public void logout() {
        try {
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
    public void showFailure(String error){
        labelMessage.setText(error);
        labelMessage.setTextFill(Color.rgb(255, 0, 0));
        labelMessage.setVisible(true);
        cross.setVisible(true);
        animateLabel();
        animateSymbol(cross);
    }
    public void showSuccess(String message) {
        labelMessage.setText("  "+message+"!");
        labelMessage.setVisible(true);
        labelMessage.setTextFill(Color.web("#009a12"));
        tick.setVisible(true);
        // Call the method to animate the label
        animateLabel();
        animateSymbol(tick);
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

    public void populateTable(HashMap<String,String> k) throws SQLException {
        ObservableList<Group> groups = FXCollections.observableArrayList();
        ArrayList<String> nn = getUserGroups(Integer.parseInt(k.get("userid")));
        for(String element :getUserGroups(Integer.parseInt(k.get("userid")) )){
            System.out.println(element);
            Group newgrp = new Group();
            newgrp.setGroup(element);
            groups.add(newgrp);
        }
        tableGroup.setItems(groups);
        newgroup.setCellValueFactory(f -> f.getValue().groupProperty());
    }
    public int userAlreadyExists(String userName){
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT User_ID from user where User_Name=\""+userName+"\"");
            rs.next();
            System.out.println(rs.getInt("User_ID"));
            return rs.getInt("User_ID");
        }catch(Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }
    public void createTransaction() throws SQLException {
        int receiverID;
        if (!(receiverChoice.getValue() == null)) {
           receiverID = userAlreadyExists(receiverChoice.getValue());
        }
        else{
            receiverID = 0;
        }
        createTransactionsec("Expenditure", cateParent.getText(), userDetails.get("userid"), receiverID ,amount.getText(),description.getText());
        setBalance(String.valueOf(getBalence((Integer.parseInt(userDetails.get("userid"))))));
        setExpenditure(userDetails);
    }
    public void createTransactionsec(String Type, String Category, String senderId, int ReceiverId, String Amount, String Description){
//        try{
//
//            String username = "griffin";
//            String password = "root";
//            String database_name = "javafx";
//            String url = "jdbc:mysql://localhost/" + database_name;
//            Connection con = DriverManager.getConnection(url,username,password);
//            PreparedStatement ps = con.prepareStatement("Insert into Transactions(Type, Category, sender, Receiver, Amount, Description, Date) values(?,?,?,?,?,?,curdate())");
//            ps.setString(1, Type);
//            ps.setString(2, Category);
//            ps.setString(3, senderId);
//            ps.setString(4, String.valueOf(ReceiverId));
//            ps.setString(5, Amount);
//            ps.setString(6, Description);
//            ps.executeUpdate();
//            table();
//            showSuccess("Expense Added!");
//        }catch(Exception e){
//            e.printStackTrace();
//        }

//            public static void createTransaction(String Type, String Category, int senderId, int ReceiverId, int Amount, String Description){
                try{
                    DatabaseConnection db = new DatabaseConnection();
                    Connection con = db.getConnection();
                    PreparedStatement ps = con.prepareStatement("Insert into Transactions(Type, Category, sender, Receiver, Amount, Description, Date) values(?,?,?,?,?,?,curdate())");
                    ps.setString(1, Type);
                    ps.setString(2, Category);
                    ps.setString(3, String.valueOf(senderId));
                    ps.setString(4, String.valueOf(ReceiverId));
                    ps.setString(5, String.valueOf(Amount));
                    ps.setString(6, Description);
                    ps.executeUpdate();

                    Statement st = con.createStatement();
                    st.executeUpdate("Update user set balence_amount= balence_Amount-"+ Amount + " where user_Id = "+senderId);
                    st.executeUpdate("Update user set balence_amount= balence_Amount+"+ Amount + " where user_Id = "+ReceiverId);
                    table();
                    showSuccess("Expense Added!");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }


    public String getReceiverName(int rec_ID){
        try{
            if(rec_ID==0) return "Nil";
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT User_Name from user where User_ID = "+rec_ID);
            rs.next();
            return rs.getString(1);
        }catch(Exception e){
            return "Nil";
        }
    }
    public static ArrayList<String> getUserGroups(int user_Id){
        ArrayList<String> s =new ArrayList<>();
        try{
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select name from groupofusers where group_id in (select group_id from groupjoin where user_Id ="+user_Id+")");
            while(rs.next()){
                s.add(rs.getString(1));
            }
            rs = st.executeQuery("select name from groupofusers where created_by ="+user_Id);
            while(rs.next()){
                s.add(rs.getString(1));
            }
            return s;
        }catch(Exception e){
            e.printStackTrace();
        }
        return s;
    }
//    public ArrayList<String> getUserGroups(int user_Id){
//        ArrayList<String> s =new ArrayList<>();
//        System.out.println(user_Id);
//        System.out.println("user_id");
//        try{
//            DatabaseConnection connectNow = new DatabaseConnection();
//            Connection connection = connectNow.getConnection();
//            Statement st = connection.createStatement();
//            ResultSet rs = st.executeQuery("select name from groupofusers where group_id in (select group_id from groupjoin where user_Id =\""+user_Id+"\"");
//            while(rs.next()){
//                s.add(rs.getString(1));
//            }
//            return s;
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return s;
//    }

    public void table() {
        ObservableList<UserExpenses> AllExpenses = FXCollections.observableArrayList();
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT Type, category, receiver, amount,description,Date FROM transactions where sender="+userDetails.get("userid")+";");

            if (!(receiverChoice.getValue() == null)) {
                if (userAlreadyExists(receiverChoice.getValue()) == 0) {
                    showFailure("  Receiver Doesn't Exist!");
                }
            }
            ResultSet rs = preparedStatement.executeQuery();
            {

                while (rs.next()) {
                    UserExpenses one_expense = new UserExpenses();
                    one_expense.setAmount(rs.getInt("amount"));
                    one_expense.setType(rs.getString("type"));
                    one_expense.setReceiver(getReceiverName(rs.getInt("receiver")));
                    one_expense.setCategory(rs.getString("category"));
                    one_expense.setDescription(rs.getString("description"));
                    one_expense.setDate(rs.getString("date"));
                    AllExpenses.add(one_expense);
                }
            }

            expenseTable.setItems(AllExpenses);
            ColumnAmount.setCellValueFactory(f -> f.getValue().amountProperty().asObject());
            ColumnType.setCellValueFactory(f -> f.getValue().typeProperty());
            ColumnReceiver.setCellValueFactory(f -> f.getValue().receiverProperty());
            ColumnCategory.setCellValueFactory(f -> f.getValue().CategoryProperty());
            ColumnDescription.setCellValueFactory(f -> f.getValue().descriptionProperty());
            ColumnDate.setCellValueFactory(f -> f.getValue().dateProperty());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void redirectAnalytics(){
        try{


            FXMLLoader analyticsLoader = new FXMLLoader(Main.class.getResource("Analytics.fxml"));

            Scene scene = new Scene(analyticsLoader.load(), 1280, 700);
            AnalyticsController analyticsController = analyticsLoader.getController();
            System.out.println("in dashboard");
            System.out.println(userDetails);
            analyticsController.setPrimaryStage(this.primaryStage);
            analyticsController.setUserID(userDetails);
            analyticsController.init();


            primaryStage.setTitle("Analytics");
            primaryStage.setScene(scene);

        } catch (Exception e) {
//            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public void redirectGroup(){
        try{

            FXMLLoader signupLoader = new FXMLLoader(Main.class.getResource("AddGroup.fxml"));

            Scene scene = new Scene(signupLoader.load(), 1280, 700);
            System.out.println("before");
            GroupController groupController = signupLoader.getController();
            groupController.setPrimaryStage(this.primaryStage);

            groupController.setDetails(userDetails);
            System.out.println("redirecting");
            groupController.init();
            primaryStage.setTitle("Add Group");
            primaryStage.setScene(scene);

        } catch (Exception e) {
//            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public void populateGroup() {
        ObservableList<UserExpenses> AllExpenses = FXCollections.observableArrayList();
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT Type, category, receiver, amount,description,Date FROM transactions;"
            );
            if (!(receiverChoice.getValue() == null)){
                if (userAlreadyExists(receiverChoice.getValue()) == 0) {
                    showFailure("  Receiver Doesn't Exist!");
                }
            }
            ResultSet rs = preparedStatement.executeQuery();
            {

                while (rs.next()) {
                    UserExpenses one_expense = new UserExpenses();
                    one_expense.setAmount(rs.getInt("amount"));
                    one_expense.setType(rs.getString("type"));
                    one_expense.setReceiver(getReceiverName(rs.getInt("receiver")));
                    one_expense.setCategory(rs.getString("category"));
                    one_expense.setDescription(rs.getString("description"));
                    one_expense.setDate(rs.getString("date"));
                    AllExpenses.add(one_expense);
                }
            }

            expenseTable.setItems(AllExpenses);
            ColumnAmount.setCellValueFactory(f -> f.getValue().amountProperty().asObject());
            ColumnType.setCellValueFactory(f -> f.getValue().typeProperty());
            ColumnReceiver.setCellValueFactory(f -> f.getValue().receiverProperty());
            ColumnCategory.setCellValueFactory(f -> f.getValue().CategoryProperty());
            ColumnDescription.setCellValueFactory(f -> f.getValue().descriptionProperty());
            ColumnDate.setCellValueFactory(f -> f.getValue().dateProperty());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}