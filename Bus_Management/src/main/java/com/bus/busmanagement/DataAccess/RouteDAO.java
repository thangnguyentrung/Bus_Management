package com.bus.busmanagement.DataAccess;

import com.bus.busmanagement.Entity.Bus;
import com.bus.busmanagement.Entity.Route;
import com.bus.busmanagement.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class RouteDAO implements BusManagement<Route>{
    private static final Connection connect = MySQLConnection.getConnection();

    @Override
    public int add(Route route) {
        try{
            if (connect != null){
                String query = "INSERT INTO route (name,origin,destination,start_date,end_date) VALUES (?,?,?,?,?)";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,route.getName());
                preparedStatement.setString(2,route.getOrigin());
                preparedStatement.setString(3,route.getDestination());
                preparedStatement.setDate(4, Date.valueOf(route.getStartDate()));
                preparedStatement.setDate(5,Date.valueOf(route.getEndDate()));
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        }catch (SQLIntegrityConstraintViolationException e){
            return -1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Route route) {
        try{
            if (connect != null){
                String query = "UPDATE route SET name = ?, origin = ?, destination = ?, start_date = ?, end_date = ? WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,route.getName());
                preparedStatement.setString(2,route.getOrigin());
                preparedStatement.setString(3,route.getDestination());
                preparedStatement.setDate(4,Date.valueOf(route.getStartDate()));
                preparedStatement.setDate(5,Date.valueOf(route.getEndDate()));
                preparedStatement.setInt(6,route.getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Route route) {
        try{
            if (connect != null){
                String query = "DELETE FROM route WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,route.getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        }catch (SQLIntegrityConstraintViolationException e){
            return -1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ObservableList<Route> getAll() {
        ObservableList<Route> routes = FXCollections.observableArrayList();
        try {
            if (connect != null) {
                String query = "SELECT * FROM route";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Route route = new Route();
                    route.setId(resultSet.getInt("id"));
                    route.setName(resultSet.getString("name"));
                    route.setOrigin(resultSet.getString("origin"));
                    route.setDestination(resultSet.getString("destination"));
                    route.setStartDate(resultSet.getDate("start_date").toLocalDate());
                    route.setEndDate(resultSet.getDate("end_date").toLocalDate());
                    routes.add(route);
                }
                return routes;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

        @Override
    public Route getByID(int id) {
            Route route = new Route();
        try {
            if (connect != null) {
                String query = "SELECT * FROM route WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    route.setId(resultSet.getInt("id"));
                    route.setName(resultSet.getString("name"));
                    route.setOrigin(resultSet.getString("origin"));
                    route.setDestination(resultSet.getString("destination"));
                    route.setStartDate(resultSet.getDate("start_date").toLocalDate());
                    route.setEndDate(resultSet.getDate("end_date").toLocalDate());
                }
                return route;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public ObservableList<Route> searchByKeyWord(String keyword){
        ObservableList<Route> routes = FXCollections.observableArrayList();
        String removeSpaceKeyword = keyword.replace(" ","");
        try {
            if (connect != null) {
                String query = "SELECT * FROM route WHERE name LIKE ? OR origin LIKE ? OR destination LIKE ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,"%"+removeSpaceKeyword+"%");
                preparedStatement.setString(2,"%"+removeSpaceKeyword+"%");
                preparedStatement.setString(3,"%"+removeSpaceKeyword+"%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Route route = new Route();
                    route.setId(resultSet.getInt("id"));
                    route.setName(resultSet.getString("name"));
                    route.setOrigin(resultSet.getString("origin"));
                    route.setDestination(resultSet.getString("destination"));
                    route.setStartDate(resultSet.getDate("start_date").toLocalDate());
                    route.setEndDate(resultSet.getDate("end_date").toLocalDate());
                    routes.add(route);
                }
                return routes;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
