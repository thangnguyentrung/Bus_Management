package com.bus.busmanagement.DataAccess;

import com.bus.busmanagement.Entity.Admin;
import com.bus.busmanagement.MySQLConnection;
import javafx.collections.ObservableList;

import java.sql.*;

public class AdminDAO implements BusManagement<Admin>{
    private static final Connection connect = MySQLConnection.getConnection();
    @Override
    public int add(Admin admin) {
        if(connect!=null){
            String query = "INSERT INTO admin(name,password) VALUES (?,?)";
            try {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,admin.getUserName());
                preparedStatement.setString(2,admin.getPassword());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
            catch (SQLIntegrityConstraintViolationException e){
                return -1;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    @Override
    public int update(Admin admin) {
        return 0;
    }

    @Override
    public int delete(Admin admin) {
        return 0;
    }

    @Override
    public ObservableList<Admin> getAll() {
        return null;
    }

    @Override
    public Admin getByID(int id) {
        if(connect!=null){
            String query = "SELECT * FROM admin WHERE id = ?";
            try {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    Admin admin = new Admin();
                    admin.setId(resultSet.getInt("id"));
                    admin.setUserName(resultSet.getString("name"));
                    admin.setPassword(resultSet.getString("password"));
                    return admin;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Admin getByUsername(String username) {
        if(connect!=null){
            String query = "SELECT * FROM admin WHERE name = ?";
            try {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    Admin admin = new Admin();
                    admin.setId(resultSet.getInt("id"));
                    admin.setUserName(resultSet.getString("name"));
                    admin.setPassword(resultSet.getString("password"));
                    return admin;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
