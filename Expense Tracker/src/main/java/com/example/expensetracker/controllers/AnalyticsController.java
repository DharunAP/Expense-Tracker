package com.example.expensetracker.controllers;
import com.example.expensetracker.DatabaseConnection;
import com.example.expensetracker.Main;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

    public class AnalyticsController{

        @FXML
        private PieChart MonthChart;

        @FXML
        private Button MonthlyBtn;

        @FXML
        private PieChart WeekChart;

        @FXML
        private PieChart YearChart;

        @FXML
        private Button YearlyBtn;

        @FXML
        private BarChart<?, ?> barChart;

        @FXML
        private Button weeklyBtn;

        @FXML
        private ChoiceBox<String> catList;
        public HashMap<String,String> userDetails;
        public void setUserID(HashMap<String,String> k) {
            this.userDetails = k;
            System.out.println("print identifier");
            System.out.println(this.userDetails);
        }
        private Stage primaryStage;
        public void setPrimaryStage(Stage stage){
            this.primaryStage = stage;
        }
        public static int getLoginId(){
            try {
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select user_ID from curruser");
                rs.next();
                return rs.getInt(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
        private final int userId = getLoginId();
        ArrayList<String> yearColor = new ArrayList<>(Arrays.asList("#9b59b6", "#2ecc71", "#e67e22", "#e74c3c", "#F875AA", "#EE7214", "#092635", "#65451F", "#FAF6F0", "#000000", "#DC8686"));

        ArrayList<String> weekColor = new ArrayList<>(Arrays.asList("#3498db", "#f39c12", "#9b59b6", "#f1c40f", "#e67e22", "#2ecc71", "#e74c3c"));
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
        public void redirectBack(){
            try{
                FXMLLoader dashLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
                Scene scene = new Scene(dashLoader.load(), 1280, 700);

                DashboardController dashController = dashLoader.getController();
                dashController.setPrimaryStage(this.primaryStage);
                dashController.setDetails(getUser(Integer.parseInt(userDetails.get("userid"))));
                dashController.init();
                primaryStage.setTitle("Dashboard");
                primaryStage.setScene(scene);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public void setMonthChart(){
            int month = 12;
            try{
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select category, sum(amount) as amt from Transactions where Type=\"Expenditure\" and sender="+userId+" and month(date)="+month+" group by Category;");
                ObservableList<PieChart.Data> pcd = FXCollections.observableArrayList();
                while(rs.next()){
                    String cat = rs.getString("category");
                    pcd.add(new PieChart.Data(cat, rs.getInt("amt")));
                }
                MonthChart.getData().addAll(pcd);
                MonthChart.setTitle("This month's expense..");
                MonthChart.setLabelsVisible(true);
                MonthChart.setStartAngle(180);
                rs = st.executeQuery("SELECT sum(amount) as sum from transactions where Type=\"Expenditure\" and sender="+userId+" and month(date)="+month);
                rs.next();
                double sum = rs.getDouble("sum");
                for (PieChart.Data slice : MonthChart.getData()) {
                    slice.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                        Tooltip tp =new Tooltip(String.format("%.2f",(slice.getPieValue()/sum)*100)+"%");
                        Tooltip.install(slice.getNode(), tp);
                    });
                }
                // System.out.println(MonthChart);
            }catch(Exception e){
                // e.printStackTrace();
            }
        }

        public void setYearChart(){
            try{
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select category, sum(amount) as amt from Transactions where Type=\"Expenditure\" and sender="+userId+" and year(date) = year(curdate()) group by Category;");
                ObservableList<PieChart.Data> pcd = FXCollections.observableArrayList();
                while(rs.next()){
                    String cat = rs.getString("category");
                    pcd.add(new PieChart.Data(cat, rs.getInt("amt")));
                }
                YearChart.getData().addAll(pcd);
                YearChart.setTitle("This year's expense..");
                YearChart.setLabelsVisible(true);
                YearChart.setStartAngle(180);
                rs = st.executeQuery("SELECT sum(amount) as sum from transactions where Type=\"Expenditure\" and sender="+userId+" and year(date) = year(curdate())");
                rs.next();
                double sum = rs.getDouble("sum");
                int i=0;
                for (PieChart.Data slice : YearChart.getData()) {
                    slice.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                        Tooltip tp =new Tooltip(String.format("%.2f",(slice.getPieValue()/sum)*100)+"%");
                        Tooltip.install(slice.getNode(), tp);
                    });
                    slice.getNode().setStyle(
                            "-fx-pie-color: " + yearColor.get(i % yearColor.size()) + ";"
                    );
                    i++;
                }
            }catch(Exception e){
                // e.printStackTrace();
            }
        }

        public void setWeekGraph(){

            try{
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select category, sum(amount) as amt from Transactions where Type=\"Expenditure\" and sender="+userId+" and week(date) = week(curdate()) group by Category;");
                ObservableList<PieChart.Data> pcd = FXCollections.observableArrayList();
                while(rs.next()){
                    String cat = rs.getString("category");
                    pcd.add(new PieChart.Data(cat, rs.getInt("amt")));
                }
                WeekChart.getData().addAll(pcd);
                WeekChart.setTitle("This week's expense..");
                WeekChart.setLabelsVisible(true);
                WeekChart.setStartAngle(180);
                rs = st.executeQuery("SELECT sum(amount) as sum from transactions where Type=\"Expenditure\" and sender="+userId+" and week(date) = week(curdate())");
                rs.next();
                double sum = rs.getDouble("sum");
                int i=0;
                for (PieChart.Data slice : WeekChart.getData()) {
                    slice.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                        Tooltip tp =new Tooltip(String.format("%.2f",(slice.getPieValue()/sum)*100)+"%");
                        Tooltip.install(slice.getNode(), tp);
                    });
                    slice.getNode().setStyle(
                            "-fx-pie-color: " + weekColor.get(i % weekColor.size()) + ";"
                    );
                    i++;
                }
            }catch(Exception e){
                // e.printStackTrace();
            }
        }

        public ArrayList<String> getCatUser(int user_ID){
            ArrayList<String> s = new ArrayList<>();
            try {
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT DISTINCT category from transactions where sender = "+user_ID);
                while (rs.next()) {
                    s.add(rs.getString(1));
                }
            } catch (Exception e) {
                // TODO: handle exception
                // e.printStackTrace();
            }
            return s;
        }

        public void setBar(){
            barChart.getData().clear();
            String category = "";

            System.out.println("setBarChart");
            try{
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                Statement st = con.createStatement();

                XYChart.Series ds = new XYChart.Series<>();
                List<String> months = new ArrayList<String>();
                months.add("January");months.add("February");months.add("March");months.add("April");months.add("May");
                months.add("June");months.add("July");months.add("August");months.add("September");months.add("October");months.add("November");
                months.add("December");
                ds.setName("");
                for(int i=1;i<=months.size();i++){
                    ResultSet rs = st.executeQuery("SELECT sum(amount) as amt from Transactions where sender = "+userId+" and category=\""+category+"\" and month(date) = "+i);
                    if(rs.next()){
                        System.out.println(rs.getInt(1));
                        XYChart.Data<String,Number> data = new XYChart.Data(months.get(i-1), rs.getInt("amt"));
                        ds.getData().add(data);
                    }else{
                        XYChart.Data<String,Number> v = new XYChart.Data(months.get(i-1),0);
                        ds.getData().add(v);
                    }
                }

                barChart.getData().addAll(ds);

            }catch(Exception e){
                // e.printStackTrace();
            }
        }

        public void setBarGraph(ActionEvent event){
            barChart.getData().clear();
            String category = catList.getValue();

            System.out.println("setBarChart");
            try{
                DatabaseConnection db = new DatabaseConnection();
                Connection con = db.getConnection();
                Statement st = con.createStatement();

                XYChart.Series ds = new XYChart.Series<>();
                List<String> months = new ArrayList<String>();
                months.add("January");months.add("February");months.add("March");months.add("April");months.add("May");
                months.add("June");months.add("July");months.add("August");months.add("September");months.add("October");months.add("November");
                months.add("December");
                ds.setName("Expense for "+category);
                for(int i=1;i<=months.size();i++){
                    ResultSet rs = st.executeQuery("SELECT sum(amount) as amt from Transactions where sender = "+userId+" and category=\""+category+"\" and month(date) = "+i);
                    if(rs.next()){
                        System.out.println(rs.getInt(1));
                        XYChart.Data<String,Number> data = new XYChart.Data(months.get(i-1), rs.getInt("amt"));
                        ds.getData().add(data);
                    }else{
                        XYChart.Data<String,Number> v = new XYChart.Data(months.get(i-1),0);
                        ds.getData().add(v);
                    }
                }

                barChart.getData().addAll(ds);

                for (Object dat : ds.getData()) {
                    XYChart.Data data = (XYChart.Data) dat;
                    Text text = new Text(" "+(Number)data.getYValue());
                    data.getNode().setOnMouseEntered(even -> text.setVisible(true));
                    data.getNode().setOnMouseExited(even -> text.setVisible(false));
                    StackPane stackPane = (StackPane) data.getNode();
                    stackPane.getChildren().add(text);
                    text.setVisible(false);
                }

            }catch(Exception e){
                // e.printStackTrace();
            }
        }

        public void init(){
            try{
                YearChart.setVisible(false);
                WeekChart.setVisible(false);
                MonthChart.setVisible(true);
                setMonthChart();
                setYearChart();
                setWeekGraph();
                setBar();
                catList.getItems().addAll(getCatUser(userId));
                System.out.println(catList.getValue());
                catList.setOnAction(this::setBarGraph);
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }

//
//        @Override
//        public void initialize(URL url , ResourceBundle rb){
//        try{
//            YearChart.setVisible(false);
//            WeekChart.setVisible(false);
//            MonthChart.setVisible(true);
//            setMonthChart();
//            setYearChart();
//            setWeekGraph();
//            setBar();
//            catList.getItems().addAll(getCatUser(userId));
//            System.out.println(catList.getValue());
//            catList.setOnAction(this::setBarGraph);
//        }
//        catch(Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//
//        }


        @FXML
        void monthlyBtnClicked(ActionEvent event) {
            YearChart.setVisible(false);
            WeekChart.setVisible(false);
            MonthChart.setVisible(true);
            YearlyBtn.setStyle(" -fx-background-color:grey; -fx-border-color:white;");
            weeklyBtn.setStyle(" -fx-background-color:grey; -fx-border-color:white;");
            MonthlyBtn.setStyle(" -fx-background-color:#79AC78;");
        }

        @FXML
        void weeklyBtnClicked(ActionEvent event) {
            MonthChart.setVisible(false);
            YearChart.setVisible(false);
            WeekChart.setVisible(true);
            weeklyBtn.setStyle(" -fx-background-color:#79AC78;");
            YearlyBtn.setStyle(" -fx-background-color:grey; -fx-border-color:white;");
            MonthlyBtn.setStyle(" -fx-background-color:grey; -fx-border-color:white;");
        }

        @FXML
        void yearlyBtnClicked(ActionEvent event) {
            MonthChart.setVisible(false);
            WeekChart.setVisible(false);;
            YearChart.setVisible(true);
            YearlyBtn.setStyle(" -fx-background-color:#79AC78;");
            MonthlyBtn.setStyle(" -fx-background-color:grey; -fx-border-color:white;");
            weeklyBtn.setStyle(" -fx-background-color:grey; -fx-border-color:white;");
        }
    }
