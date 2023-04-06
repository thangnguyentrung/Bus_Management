package com.bus.busmanagement.DataAccess;

import com.bus.busmanagement.Entity.Customer;
import com.bus.busmanagement.Entity.SeatType;
import com.bus.busmanagement.Entity.Ticket;
import com.bus.busmanagement.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TicketDAO implements BusManagement<Ticket>{
    private static final Connection connect = MySQLConnection.getConnection();
    public Float getTotalIncome(){
        Float totalIncome = 0f;
        try{
            if(connect!=null){
                String query = "SELECT SUM(price) FROM ticket";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    totalIncome = resultSet.getFloat(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalIncome;
    }
    public Float getTodayIncome(){
        Float todayIncome = 0f;
        try{
            if(connect!=null){
                String query = "SELECT SUM(price) FROM ticket WHERE purchase_time LIKE ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1, "%"+LocalDate.now()+"%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    todayIncome = resultSet.getFloat(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return todayIncome;
    }
    @Override
    public int add(Ticket ticket) {
        try{
            if(connect!=null){
                String query = "INSERT INTO ticket(seat_number,seat_type,price,purchase_time,bus_id,customer_id) VALUES (?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,ticket.getSeatNumber());
                preparedStatement.setString(2,ticket.getSeatType().name());
                preparedStatement.setFloat(3,ticket.getPrice());
                preparedStatement.setString(4,ticket.getPurcharseTime().toString());
                preparedStatement.setInt(5,ticket.getBus().getId());
                preparedStatement.setInt(6,ticket.getCustomer().getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        } catch (SQLIntegrityConstraintViolationException e){
            return -1;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int update(Ticket ticket) {
        try{
            if(connect!=null){
                String query = "UPDATE ticket SET seat_number = ?, seat_type = ?, price = ?, purchase_time = ?, bus_id = ?, customer_id = ? WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,ticket.getSeatNumber());
                preparedStatement.setString(2,ticket.getSeatType().name());
                preparedStatement.setFloat(3,ticket.getPrice());
                preparedStatement.setString(4,ticket.getPurcharseTime().toString());
                preparedStatement.setInt(5,ticket.getBus().getId());
                preparedStatement.setInt(6,ticket.getCustomer().getId());
                preparedStatement.setInt(7,ticket.getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int delete(Ticket ticket) {
        try{
            if(connect!=null){
                String query = "DELETE FROM ticket WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,ticket.getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public void deleteTicketByCustomer(Ticket ticket){
        try{
            if(connect!=null){
                String query = "DELETE FROM ticket WHERE customer_id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,ticket.getCustomer().getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Ticket> getAll() {
        ObservableList<Ticket> tickets = FXCollections.observableArrayList();
        try{
            if(connect!=null){
                String query = "SELECT * FROM ticket";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Ticket ticket = new Ticket();
                    ticket.setId(resultSet.getInt("id"));
                    ticket.setSeatNumber(resultSet.getString("seat_number"));
                    ticket.setSeatType(SeatType.valueOf(resultSet.getString("seat_type")));
                    ticket.setPrice(resultSet.getFloat("price"));
                    ticket.setPurcharseTime(resultSet.getTimestamp("purchase_time").toLocalDateTime());
                    ticket.setBus(new BusDAO().getByID(resultSet.getInt("bus_id")));
                    ticket.setCustomer(new CustomerDAO().getByID(resultSet.getInt("customer_id")));
                    tickets.add(ticket);
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Ticket getByID(int id) {
        Ticket ticket = new Ticket();
        try{
            if(connect!=null){
                String query = "SELECT * FROM ticket WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    ticket.setId(resultSet.getInt("id"));
                    ticket.setSeatNumber(resultSet.getString("seat_number"));
                    ticket.setSeatType(SeatType.valueOf(resultSet.getString("seat_type")));
                    ticket.setPrice(resultSet.getFloat("price"));
                    ticket.setPurcharseTime(resultSet.getTimestamp("purchase_time").toLocalDateTime());
                    ticket.setBus(new BusDAO().getByID(resultSet.getInt("bus_id")));
                    ticket.setCustomer(new CustomerDAO().getByID(resultSet.getInt("customer_id")));
                }
                return ticket;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public Ticket getTicketBySeatNumberAndBusID(String seatNumber, int busID){
        Ticket ticket = new Ticket();
        try{
            if(connect!=null){
                String query = "SELECT * FROM ticket WHERE seat_number = ? AND bus_id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,seatNumber);
                preparedStatement.setInt(2,busID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    ticket.setId(resultSet.getInt("id"));
                    ticket.setSeatNumber(resultSet.getString("seat_number"));
                    ticket.setSeatType(SeatType.valueOf(resultSet.getString("seat_type")));
                    ticket.setPrice(resultSet.getFloat("price"));
                    ticket.setPurcharseTime(resultSet.getTimestamp("purchase_time").toLocalDateTime());
                    ticket.setBus(new BusDAO().getByID(resultSet.getInt("bus_id")));
                    ticket.setCustomer(new CustomerDAO().getByID(resultSet.getInt("customer_id")));
                }
                return ticket;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    // check if seat is available
    public boolean isSeatAvailable(String seatNumber, int busID){
        try{
            if(connect!=null){
                String query = "SELECT * FROM ticket WHERE seat_number = ? AND bus_id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,seatNumber);
                preparedStatement.setInt(2,busID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    // count number of VIP seats
    public int countVIPSeats(int busID){
        int count = 0;
        try{
            if(connect!=null){
                String query = "SELECT * FROM ticket WHERE seat_type = ? AND bus_id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,SeatType.VIP.name());
                preparedStatement.setInt(2,busID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    count++;
                }
                return count;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    // count number of normal seats
    public  int countNormalSeats(int busID){
        int count = 0;
        try{
            if(connect!=null){
                String query = "SELECT * FROM ticket WHERE seat_type = ? AND bus_id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,SeatType.NORMAL.name());
                preparedStatement.setInt(2,busID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    count++;
                }
                return count;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    // count total number of seats
    public int countSeats(int busID){
        int count = 0;
        try{
            if(connect!=null){
                String query = "SELECT * FROM ticket WHERE bus_id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,busID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    count++;
                }
                return count;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

}
