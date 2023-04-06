package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.DisplayInforDAO;
import com.bus.busmanagement.Entity.Bus;
import com.bus.busmanagement.Entity.DisplayInformation;
import javafx.collections.ObservableList;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    TableView<DisplayInformation> informationTableView;
    @FXML
    TableColumn<DisplayInformation, Integer> customer_id;
    @FXML
    TableColumn<DisplayInformation, Integer> ticket_id;
    @FXML
    TableColumn<DisplayInformation, String> fullName;
    @FXML
    TableColumn<DisplayInformation,String> gender;
    @FXML
    TableColumn<DisplayInformation,String> phone;
    @FXML
    TableColumn<DisplayInformation,String> route;
    @FXML
    TableColumn<DisplayInformation,String> busName;
    @FXML
    TableColumn<DisplayInformation,String> seatNumber;
    @FXML
    TableColumn<DisplayInformation,String> seatType;
    @FXML
    TableColumn<DisplayInformation,LocalDateTime> purchaseTime;
    private DisplayInforDAO displayInforDAO = new DisplayInforDAO();
    private ObservableList<DisplayInformation> displayInformations = displayInforDAO.getAll();
    @FXML
    private FontIcon btnSearch;
    @FXML
    private FontIcon refreshIcon;
    @FXML
    private TextField searchField;
    public void search(String keyword){
        if(keyword.isEmpty()){
            informationTableView.setItems(displayInformations);
        }else {
            informationTableView.setItems(displayInforDAO.searchByKeyword(keyword));
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customer_id.setCellValueFactory(new PropertyValueFactory<DisplayInformation,Integer>("customer_id"));
        ticket_id.setCellValueFactory(new PropertyValueFactory<DisplayInformation,Integer>("ticket_id"));
        fullName.setCellValueFactory(new PropertyValueFactory<DisplayInformation,String>("fullName"));
        gender.setCellValueFactory(new PropertyValueFactory<DisplayInformation,String>("gender"));
        phone.setCellValueFactory(new PropertyValueFactory<DisplayInformation,String>("phone"));
        route.setCellValueFactory(new PropertyValueFactory<DisplayInformation,String>("route"));
        busName.setCellValueFactory(new PropertyValueFactory<DisplayInformation,String>("busName"));
        seatNumber.setCellValueFactory(new PropertyValueFactory<DisplayInformation,String>("seatNumber"));
        seatType.setCellValueFactory(new PropertyValueFactory<DisplayInformation,String>("seatType"));
        purchaseTime.setCellValueFactory(new PropertyValueFactory<DisplayInformation,LocalDateTime>("purchaseTime"));
        // format purchaseTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm   dd-MM-yyyy");
        purchaseTime.setCellFactory(column -> {
            return new TableCell<DisplayInformation, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String formattedDateTime = item.format(formatter);
                        setText(formattedDateTime);
                    }
                }
            };
        });

        informationTableView.setItems(displayInformations);
        // SEARCH FUNCTION
        btnSearch.setOnMouseClicked(event -> {
            search(searchField.getText());
        });
        searchField.setOnKeyReleased(event -> {
            search(searchField.getText());
        });
        // REFRESH FUNCTION
        refreshIcon.setOnMouseClicked(event -> {
            informationTableView.setItems(displayInforDAO.getAll());
        });
    }

    public void viewInformation(){
        DisplayInformation displayInformation = informationTableView.getSelectionModel().getSelectedItem();
        if(displayInformation != null){
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bus/busmanagement/Customer/Customer-Update.fxml"));
            try{
                AnchorPane viewUpdateCustomer = loader.load();
                Scene scene = new Scene(viewUpdateCustomer);
                CustomerUpdateController customerUpdateController = loader.getController();
                customerUpdateController.getCustomerInformation(displayInformation);
                stage.setTitle("Customer Information");
                stage.setScene(scene);
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a row");
            alert.showAndWait();
        }
    }
}
