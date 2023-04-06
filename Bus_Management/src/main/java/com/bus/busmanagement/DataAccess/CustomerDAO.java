package com.bus.busmanagement.DataAccess;

import com.bus.busmanagement.Entity.Customer;
import com.bus.busmanagement.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;

public class CustomerDAO implements BusManagement<Customer> {
    private static final Connection connect = MySQLConnection.getConnection();

    @Override
    public int add(Customer customer) {
        try{
            if(connect!=null){
                String query = "INSERT INTO customer(name,phone,email ,gender) VALUES (?,?,?,?)";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,customer.getName());
                preparedStatement.setString(2,customer.getPhone());
                preparedStatement.setString(3,customer.getEmail());
                preparedStatement.setString(4,customer.getGender().toString());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        }
        catch (SQLIntegrityConstraintViolationException e) {
                return -1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Customer customer) {
        try {
            if(connect!=null){
                String query = "UPDATE customer SET name = ?, phone  = ?, email  = ?, gender  = ? WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,customer.getName());
                preparedStatement.setString(2,customer.getPhone());
                preparedStatement.setString(3,customer.getEmail());
                preparedStatement.setString(4,customer.getGender().toString());
                preparedStatement.setInt(5,customer.getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int delete(Customer customer) {
        try {
            if(connect!=null){
                String query = "DELETE FROM customer WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,customer.getId());
                int rows = preparedStatement.executeUpdate();
                return rows;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public ObservableList<Customer> getAll() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try{
            if (connect!=null){
                String query = "SELECT * FROM customer";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setId(resultSet.getInt("id"));
                    customer.setName(resultSet.getString("name"));
                    customer.setPhone(resultSet.getString("phone"));
                    customer.setEmail(resultSet.getString("email"));
                    customer.setGender(resultSet.getString("gender"));
                    customers.add(customer);
                }
                return customers;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Customer getByID(int id) {
        Customer customer = new Customer();
        try{
            if (connect!=null){
                String query = "SELECT * FROM customer WHERE id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    customer.setId(resultSet.getInt("id"));
                    customer.setName(resultSet.getString("name"));
                    customer.setPhone(resultSet.getString("phone"));
                    customer.setEmail(resultSet.getString("email"));
                    customer.setGender(resultSet.getString("gender"));
                }
                return customer;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int getCustomerID(String customerName) {
        try{
            if (connect!=null){
                String query = "SELECT id FROM customer WHERE name = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1,customerName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
