package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.BusDAO;
import com.bus.busmanagement.DataAccess.DisplayInforDAO;
import com.bus.busmanagement.DataAccess.TicketDAO;
import com.bus.busmanagement.FxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.geom.Area;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private BorderPane mainView;
    @FXML
    private AreaChart<String,Integer> incomeChart;
    @FXML
    private Label availableBuses;
    @FXML
    private Label totalBuses;
    @FXML
    private Label totalIncome;
    @FXML
    private Label todayIncome;
    private BusDAO busDAO = new BusDAO();
    private TicketDAO ticketDAO = new TicketDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // DISPLAY CHART
        DisplayInforDAO displayInforDAO = new DisplayInforDAO();
        displayInforDAO.displayChart();
        incomeChart.getData().add(displayInforDAO.displayChart());
        // DISPLAY NUMBER OF BUSES
        availableBuses.setText(String.valueOf(busDAO.getTotalAvailableBus()));
        totalBuses.setText(String.valueOf(busDAO.getTotalBus()));
        // DISPLAY INCOME AND FORMAT TO VNĐ
        totalIncome.setText(String.format("%,.0f",ticketDAO.getTotalIncome())+" đ");
        todayIncome.setText(String.format("%,.0f",ticketDAO.getTodayIncome())+" đ");

    }
    public void directToHomeView(ActionEvent event) {
        FxmlLoader fxmlLoader = new FxmlLoader();
        AnchorPane view = fxmlLoader.getPage("/com/bus/busmanagement/Home-View.fxml");
        mainView.setCenter(view);
    }
    public void directToRouteView(ActionEvent event) {
        FxmlLoader fxmlLoader = new FxmlLoader();
        AnchorPane view = fxmlLoader.getPage("/com/bus/busmanagement/Route/Route-View.fxml");
        mainView.setCenter(view);
    }
    public void directToBusesView(ActionEvent event) {
        FxmlLoader fxmlLoader = new FxmlLoader();
        AnchorPane view = fxmlLoader.getPage("/com/bus/busmanagement/Buses/Buses-View.fxml");
        mainView.setCenter(view);
    }
    public void directToTicketView(ActionEvent event) {
        FxmlLoader fxmlLoader = new FxmlLoader();
        AnchorPane view = fxmlLoader.getPage("/com/bus/busmanagement/Ticket/Ticket-View.fxml");
        mainView.setCenter(view);
    }
    public void directToCustomerView(ActionEvent event) {
        FxmlLoader fxmlLoader = new FxmlLoader();
        AnchorPane view = fxmlLoader.getPage("/com/bus/busmanagement/Customer/Customer-View.fxml");
        mainView.setCenter(view);
    }
    public void directToLoginView(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bus/busmanagement/Login/Login.fxml"));
        try {
            Pane root = loader.load();
            LoginController loginController = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Set the stage in the middle of the screen
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}