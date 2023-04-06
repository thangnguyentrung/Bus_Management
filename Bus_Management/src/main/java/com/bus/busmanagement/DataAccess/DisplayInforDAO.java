package com.bus.busmanagement.DataAccess;

import com.bus.busmanagement.Entity.DisplayInformation;
import com.bus.busmanagement.Entity.Ticket;
import com.bus.busmanagement.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DisplayInforDAO implements BusManagement<DisplayInformation>{
    private static final Connection connect = MySQLConnection.getConnection();
    // DISPLAY DATA TO CHART
    public XYChart.Series<String, Integer> displayChart(){
        XYChart.Series<String, Integer> chartData = new XYChart.Series<>();
        try {
            if(connect!=null){
                String query = "SELECT DATE_FORMAT(t.purchase_time, '%Y-%m-%d') AS date, SUM(t.price) AS revenue\n" +
                        "FROM ticket t\n" +
                        "         INNER JOIN bus b ON t.bus_id = b.id\n" +
                        "         INNER JOIN route r ON b.route_id = r.id\n" +
                        "GROUP BY DATE_FORMAT(r.start_date, '%Y-%m-%d')";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    chartData.getData().add(new XYChart.Data<>(resultSet.getString("date"),resultSet.getInt("revenue")));
                }
                return chartData;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @Override
    public int add(DisplayInformation displayInformation) {
        return 0;
    }

    @Override
    public int update(DisplayInformation displayInformation) {
        return 0;
    }

    @Override
    public int delete(DisplayInformation displayInformation) {
        return 0;
    }

    @Override
    public ObservableList<DisplayInformation> getAll() {
        ObservableList<DisplayInformation> displayInformations = FXCollections.observableArrayList();
        try{
            if(connect!=null){
                String query = "SELECT ticket.customer_id, ticket.id as ticket_id," +
                        " customer.name, customer.gender, customer.phone, route.name as route_name," +
                        " bus.name as bus_name, ticket.seat_number, ticket.seat_type, ticket.purchase_time\n" +
                        "FROM ticket\n" +
                        "INNER JOIN bus ON ticket.bus_id = bus.id\n" +
                        "INNER JOIN customer ON ticket.customer_id = customer.id\n" +
                        "INNER JOIN route ON bus.route_id = route.id; ";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    DisplayInformation displayInformation = new DisplayInformation();
                    displayInformation.setCustomer_id(resultSet.getInt("customer_id"));
                    displayInformation.setTicket_id(resultSet.getInt("ticket_id"));
                    displayInformation.setFullName(resultSet.getString("name"));
                    displayInformation.setGender(resultSet.getString("gender"));
                    displayInformation.setPhone(resultSet.getString("phone"));
                    displayInformation.setRoute(resultSet.getString("route_name"));
                    displayInformation.setBusName(resultSet.getString("bus_name"));
                    displayInformation.setSeatNumber(resultSet.getString("seat_number"));
                    displayInformation.setSeatType(resultSet.getString("seat_type"));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    displayInformation.setPurchaseTime(LocalDateTime.parse(resultSet.getString("purchase_time"), formatter));
                    displayInformations.add(displayInformation);
                }
                return displayInformations;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public DisplayInformation getByID(int id) {
        return null;
    }
    public ObservableList<DisplayInformation> searchByKeyword(String keyword){
        ObservableList<DisplayInformation> displayInformations = FXCollections.observableArrayList();
        String removeSpaceKeyword = keyword.replace(" ","");
        try{
            if(connect!=null){
                String query = "SELECT ticket.customer_id, ticket.id as ticket_id," +
                        " customer.name, customer.gender, customer.phone, route.name as route_name," +
                        " bus.name as bus_name, ticket.seat_number, ticket.seat_type, ticket.purchase_time\n" +
                        "FROM ticket\n" +
                        "INNER JOIN bus ON ticket.bus_id = bus.id\n" +
                        "INNER JOIN customer ON ticket.customer_id = customer.id\n" +
                        "INNER JOIN route ON bus.route_id = route.id" +
                        " WHERE customer.name LIKE ? OR customer.phone LIKE ? OR route.name LIKE ? OR bus.name LIKE ?;";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1, "%" + removeSpaceKeyword + "%");
                preparedStatement.setString(2, "%" + removeSpaceKeyword + "%");
                preparedStatement.setString(3, "%" + removeSpaceKeyword + "%");
                preparedStatement.setString(4, "%" + removeSpaceKeyword + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    DisplayInformation displayInformation = new DisplayInformation();
                    displayInformation.setCustomer_id(resultSet.getInt("customer_id"));
                    displayInformation.setTicket_id(resultSet.getInt("ticket_id"));
                    displayInformation.setFullName(resultSet.getString("name"));
                    displayInformation.setGender(resultSet.getString("gender"));
                    displayInformation.setPhone(resultSet.getString("phone"));
                    displayInformation.setRoute(resultSet.getString("route_name"));
                    displayInformation.setBusName(resultSet.getString("bus_name"));
                    displayInformation.setSeatNumber(resultSet.getString("seat_number"));
                    displayInformation.setSeatType(resultSet.getString("seat_type"));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    displayInformation.setPurchaseTime(LocalDateTime.parse(resultSet.getString("purchase_time"), formatter));
                    displayInformations.add(displayInformation);
                }
                return displayInformations;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
}
