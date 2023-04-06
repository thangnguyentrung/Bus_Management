package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.BusDAO;
import com.bus.busmanagement.DataAccess.RouteDAO;
import com.bus.busmanagement.Entity.Route;

import com.bus.busmanagement.Validation.RouteValidation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPagination;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RouteController implements Initializable {
    private RouteDAO routeDAO = new RouteDAO();
    @FXML
    private TextField routeName;
    @FXML
    private TextField routeOrigin;
    @FXML
    private TextField routeDestination;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TableView<Route> routeTable = new TableView<>();
    @FXML
    private TableColumn<Route, Integer> routeIDColumn;
    @FXML
    private TableColumn<Route, String> routeNameColumn;
    @FXML
    private TableColumn<Route, String> routeOriginColumn;
    @FXML
    private TableColumn<Route, String> routeDestinationColumn;
    @FXML
    private TableColumn<Route, LocalDate> startDateColumn;
    @FXML
    private TableColumn<Route, LocalDate> endDateColumn;
    private ObservableList<Route> routeList ;
    @FXML
    private MFXPagination pagination;
    @FXML
    FontIcon btnSearch;
    @FXML
    TextField searchTextField;
    public void search(String keyword){
        if(keyword.isEmpty()){
            routeTable.setItems(routeList);
        }else {
            ObservableList<Route> searchList = routeDAO.searchByKeyWord(keyword);
            routeTable.setItems(searchList);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get list routes
        routeList = routeDAO.getAll();
        // Set value for table
        routeIDColumn.setCellValueFactory(new PropertyValueFactory<Route, Integer>("id"));
        routeNameColumn.setCellValueFactory(new PropertyValueFactory<Route, String>("name"));
        routeOriginColumn.setCellValueFactory(new PropertyValueFactory<Route, String>("origin"));
        routeDestinationColumn.setCellValueFactory(new PropertyValueFactory<Route, String>("destination"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<Route, LocalDate>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<Route, LocalDate>("endDate"));
        routeTable.setItems(routeList);
        // pagination table
        // Search
        btnSearch.setOnMouseClicked(event -> {
            search(searchTextField.getText());
        });
        searchTextField.setOnKeyReleased(event -> {
            search(searchTextField.getText());
        });

    }

    public void clearField(){
        routeName.clear();
        routeOrigin.clear();
        routeDestination.clear();
        startDate.setValue(null);
        endDate.setValue(null);
    }
    public void addRoute(){
        //VALIDATE DATA
        if(routeName.getText().isEmpty() || routeOrigin.getText().isEmpty() || routeDestination.getText().isEmpty() || startDate.getValue() == null||endDate.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
        }else if(!RouteValidation.isValidRoute(routeName.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Route name is invalid \n" +
                    " Route only allows the use of letters (unsigned), digits, spaces and dashes\n" +
                    " and must not start or end with a space or dash.");
            alert.showAndWait();
        }else if(!RouteValidation.isValidOriginOrDestination(routeOrigin.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Origin is invalid \n" +
                    "Not allow special characters except / and -");
            alert.showAndWait();
        }else if(!RouteValidation.isValidOriginOrDestination(routeDestination.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Destination is invalid \n" +
                    "Not allow special characters except / and -");
            alert.showAndWait();
        }else if(startDate.getValue().isAfter(endDate.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Start date must be before end date");
            alert.showAndWait();
        }else {
            Route route = new Route();
            route.setName(routeName.getText());
            route.setOrigin(routeOrigin.getText());
            route.setDestination(routeDestination.getText());
            route.setStartDate(startDate.getValue());
            route.setEndDate(endDate.getValue());
            int result = routeDAO.add(route);
            // Check if route name is existed
            if(result == -1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Route name is existed");
                alert.showAndWait();
            }else if(result == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Add route failed");
                alert.showAndWait();
            }else {
                routeList.add(route);
            }
            // Clear text field
            routeName.clear();
            routeOrigin.clear();
            routeDestination.clear();
            startDate.setValue(null);
            endDate.setValue(null);
        }

    }
    public void updateRoute(){
        Route route = routeTable.getSelectionModel().getSelectedItem();
        if(route == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a route to update");
            alert.showAndWait();
            return;
        }
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bus/busmanagement/Route/Route-Update.fxml"));
        try {
            AnchorPane viewUpdateRoute = loader.load();
            Scene scene = new Scene(viewUpdateRoute);
            RouteUpdateController routeUpdateController = loader.getController();
            routeUpdateController.getRoute(route);
            stage.setTitle("Update Route");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refreshTable(){
       routeTable.setItems(routeDAO.getAll());
    }
    public void deleteRoute(){
        Route route = routeTable.getSelectionModel().getSelectedItem();
        if(route == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a route to delete");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete Route");
        alert.setHeaderText("Are you sure to delete this route?");
        alert.setContentText("This action cannot be undone");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            int rs = routeDAO.delete(route);
            if(rs == -1) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setContentText("Delete route failed, maybe this route is used by a bus");
                alert1.showAndWait();
            }
        }
    }
}
