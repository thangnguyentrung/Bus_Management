package com.bus.busmanagement.DataAccess;


import com.bus.busmanagement.Entity.Bus;
import com.bus.busmanagement.Entity.Route;
import com.bus.busmanagement.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class BusDAO implements BusManagement<Bus> {
    private static final Connection connect = MySQLConnection.getConnection();
    public int getTotalAvailableBus(){
        try{
            if(connect!=null){
                String query = "SELECT COUNT(*) FROM bus WHERE status = 'AVAILABLE'";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return resultSet.getInt(1);
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return 0;
    }
    public int getTotalBus(){
        try{
            if(connect!=null){
                String query = "SELECT COUNT(*) FROM bus";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return resultSet.getInt(1);
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return 0;
    }
    @Override
    public int add(Bus bus) {
        try{
            if (connect != null){
                String query = "INSERT INTO bus (name,license_plate,capacity,VIP_seat,price,route_id,status) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,bus.getName());
                preparedStatement.setString(2,bus.getLicensePlate());
                preparedStatement.setInt(3,bus.getCapacity());
                preparedStatement.setInt(4,bus.getVipSeats());
                preparedStatement.setFloat(5,bus.getPrice());
                preparedStatement.setInt(6,bus.getRoute().getId());
                preparedStatement.setString(7, bus.getStatus().name());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int update(Bus bus) {
        try{
            if (connect != null){
                String query = "UPDATE bus SET name = ?, license_plate = ?, capacity = ?, VIP_seat = ?,price = ?, route_id = ?, status = ? WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,bus.getName());
                preparedStatement.setString(2,bus.getLicensePlate());
                preparedStatement.setInt(3,bus.getCapacity());
                preparedStatement.setInt(4,bus.getVipSeats());
                preparedStatement.setFloat(5,bus.getPrice());
                preparedStatement.setInt(6,bus.getRoute().getId());
                preparedStatement.setString(7,bus.getStatus().toString());
                preparedStatement.setInt(8,bus.getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int delete(Bus bus) {
        try{
            if (connect != null){
                String query = "DELETE FROM bus WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,bus.getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        }catch (SQLIntegrityConstraintViolationException e){
            return -1;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public void deleteBusByRouteID(Bus bus) {
        try{
            if (connect != null){
                String query = "DELETE FROM bus WHERE route_id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,bus.getRoute().getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Bus> getAll() {
        ObservableList<Bus> buses = FXCollections.observableArrayList();
        try{
            if (connect != null){
                String query = "SELECT * FROM bus";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Bus bus = new Bus();
                    bus.setId(resultSet.getInt("id"));
                    bus.setName(resultSet.getString("name"));
                    bus.setLicensePlate(resultSet.getString("license_plate"));
                    bus.setCapacity(resultSet.getInt("capacity"));
                    bus.setVipSeats(resultSet.getInt("VIP_seat"));
                    bus.setPrice(resultSet.getFloat("price"));
                    bus.setRoute(new RouteDAO().getByID(resultSet.getInt("route_id")));
                    bus.setStatus((resultSet.getString("status")));
                    buses.add(bus);
                }
                return buses;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Bus getByID(int id) {
        try{
            Bus bus = new Bus();
            if (connect != null){
                String query = "SELECT * FROM bus WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    bus.setId(resultSet.getInt("id"));
                    bus.setName(resultSet.getString("name"));
                    bus.setLicensePlate(resultSet.getString("license_plate"));
                    bus.setCapacity(resultSet.getInt("capacity"));
                    bus.setVipSeats(resultSet.getInt("VIP_seat"));
                    bus.setPrice(resultSet.getFloat("price"));
                    bus.setRoute(new RouteDAO().getByID(resultSet.getInt("route_id")));
                    bus.setStatus((resultSet.getString("status")));
                }
                return bus;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public ObservableList<Bus> getByRoute(Route route){
        ObservableList<Bus> buses = FXCollections.observableArrayList();
        try {
            if (connect != null){
                String query = "SELECT * FROM bus WHERE route_id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,route.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Bus bus = new Bus();
                    bus.setId(resultSet.getInt("id"));
                    bus.setName(resultSet.getString("name"));
                    bus.setLicensePlate(resultSet.getString("license_plate"));
                    bus.setCapacity(resultSet.getInt("capacity"));
                    bus.setVipSeats(resultSet.getInt("VIP_seat"));
                    bus.setPrice(resultSet.getFloat("price"));
                    bus.setRoute(new RouteDAO().getByID(resultSet.getInt("route_id")));
                    buses.add(bus);
                }
                return buses;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public ObservableList<Bus> searchByKeyWord(String keyword){
        ObservableList<Bus> buses = FXCollections.observableArrayList();
        String removeSpaceKeyword = keyword.replaceAll("\\s+","");
        try {
            if (connect != null){
                String query = "SELECT * FROM bus WHERE name LIKE ? OR license_plate LIKE ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,"%"+removeSpaceKeyword+"%");
                preparedStatement.setString(2,"%"+removeSpaceKeyword+"%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Bus bus = new Bus();
                    bus.setId(resultSet.getInt("id"));
                    bus.setName(resultSet.getString("name"));
                    bus.setLicensePlate(resultSet.getString("license_plate"));
                    bus.setCapacity(resultSet.getInt("capacity"));
                    bus.setVipSeats(resultSet.getInt("VIP_seat"));
                    bus.setPrice(resultSet.getFloat("price"));
                    bus.setRoute(new RouteDAO().getByID(resultSet.getInt("route_id")));
                    buses.add(bus);
                }
                return buses;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public int getVipSeatsByBusID(int id){
        try{
            if (connect != null){
                String query = "SELECT VIP_seat FROM bus WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    return resultSet.getInt("VIP_seat");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public int getNormalSeatsByBusID(int id){
        try{
            if (connect != null){
                String query = "SELECT capacity FROM bus WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    int capacity = resultSet.getInt("capacity");
                    return capacity - getVipSeatsByBusID(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
