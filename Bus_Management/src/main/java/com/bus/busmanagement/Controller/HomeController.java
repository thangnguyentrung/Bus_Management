package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.BusDAO;
import com.bus.busmanagement.DataAccess.DisplayInforDAO;
import com.bus.busmanagement.DataAccess.TicketDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label availableBuses;

    @FXML
    private AreaChart<String, Integer> incomeChart;

    @FXML
    private Label todayIncome;

    @FXML
    private Label totalBuses;

    @FXML
    private Label totalIncome;
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
}
