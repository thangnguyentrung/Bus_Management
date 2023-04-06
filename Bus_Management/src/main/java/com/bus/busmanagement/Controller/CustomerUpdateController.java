package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.BusDAO;
import com.bus.busmanagement.DataAccess.CustomerDAO;
import com.bus.busmanagement.DataAccess.RouteDAO;
import com.bus.busmanagement.DataAccess.TicketDAO;
import com.bus.busmanagement.Entity.*;
import com.bus.busmanagement.Validation.CustomerValidation;
import com.bus.busmanagement.Validation.TicketValidation;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class CustomerUpdateController implements Initializable {
    @FXML
    TextField customerIDTextField;
    @FXML
    TextField customerNameTextField;
    @FXML
    ComboBox<Gender> genderComboBox;
    @FXML
    TextField customerPhoneTextField;
    @FXML
    TextField customerEmailTextField;
    @FXML
    TextField ticketID;
    @FXML
    TextField customerIDTextField1;
    @FXML
    ComboBox<Route> routeComboBox;
    @FXML
    ComboBox<Bus> busComboBox;
    @FXML
    TextField ticketSeatNumber;
    @FXML
    ComboBox<SeatType> ticketSeatType;
    @FXML
    TextField ticketPrice;
    @FXML
    TextField ticketPurchaseTime;
    @FXML
    MFXButton btnEdit;
    @FXML
    MFXButton btnSave;
    @FXML
    MFXButton btnDelete;
    BusDAO busDAO = new BusDAO();
    TicketDAO ticketDAO = new TicketDAO();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSave.setDisable(true);
        btnEdit.setOnAction(actionEvent -> {
            // Enable all text fields
            customerNameTextField.setDisable(false);
            genderComboBox.setDisable(false);
            customerPhoneTextField.setDisable(false);
            customerEmailTextField.setDisable(false);

            routeComboBox.setDisable(false);
            busComboBox.setDisable(false);
            ticketSeatNumber.setDisable(false);
            ticketSeatType.setDisable(false);

            btnSave.setDisable(false);
            // Set value for combobox
            genderComboBox.setItems(Gender.getAll());
            ticketSeatType.setItems(SeatType.getAll());
            RouteDAO routeDAO = new RouteDAO();
            routeComboBox.setItems(routeDAO.getAll());
            routeComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, route, t1) -> {
                BusDAO busDAO = new BusDAO();
                busComboBox.setItems(busDAO.getByRoute(t1));
            });
        });
        btnSave.setOnAction(actionEvent -> {
            // UPDATE CUSTOMER
            //VALIDATE CUSTOMER INFORMATION
            if(customerNameTextField.getText().isEmpty() || customerPhoneTextField.getText().isEmpty() || customerEmailTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Submit Error");
                alert.setHeaderText("Customer information is not valid");
                alert.setContentText("Please fill all information");
                alert.showAndWait();
            } else if (!CustomerValidation.isNameValid(customerNameTextField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Submit Error");
                alert.setHeaderText("Name is not valid");
                alert.setContentText("Please use only alphabet");
                alert.showAndWait();
            }
            else if (!CustomerValidation.isPhoneValid(customerPhoneTextField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Submit Error");
                alert.setHeaderText("Phone is not valid");
                alert.setContentText("Please use only number\n atleast 10 digits");
                alert.showAndWait();
            } else if (!CustomerValidation.isEmailValid(customerEmailTextField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Submit Error");
                alert.setHeaderText("Email is not valid");
                alert.setContentText("Please use correct email format");
                alert.showAndWait();
            }else {
                CustomerDAO customerDAO = new CustomerDAO();
                Customer customer = customerDAO.getByID(Integer.parseInt(customerIDTextField.getText()));
                customer.setName(customerNameTextField.getText());
                customer.setGender(genderComboBox.getValue().toString());
                customer.setPhone(customerPhoneTextField.getText());
                customer.setEmail(customerEmailTextField.getText());

                int rs = customerDAO.update(customer);
                if(rs == -1){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Submit Error");
                    alert.setHeaderText("Phone or email has been existed");
                    alert.setContentText("Please use another phone or email");
                    alert.showAndWait();
                }
                // UPDATE TICKET
                //VALIDATE TICKET INFORMATION
                if(routeComboBox.getValue() == null || busComboBox.getValue() == null || ticketSeatNumber.getText().isEmpty() || ticketSeatType.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Submit Error");
                    alert.setHeaderText("Ticket information is not valid");
                    alert.setContentText("Please fill all information");
                    alert.showAndWait();
                } else if (!TicketValidation.isSeatNumberValid(ticketSeatNumber.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Submit Error");
                    alert.setHeaderText("Seat number is not valid");
                    alert.setContentText("Please use number from 1 to 50\n character A to D\n" +
                            "example: 1A , 3B, ... 50D");
                    alert.showAndWait();
                }
                // check if the VIP seat is full?
                else if(ticketSeatType.getValue().name().equals("VIP")&&busDAO.getVipSeatsByBusID(busComboBox.getValue().getVipSeats())-ticketDAO.countVIPSeats(busComboBox.getValue().getId())==0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Booking Error");
                    alert.setHeaderText("There are no VIP seats left");
                    alert.setContentText("Please choose another seat type");
                    alert.showAndWait();
                }
                // check if the Normal seat is full?
                else if(ticketSeatType.getValue().name().equals("NORMAL")&&busDAO.getNormalSeatsByBusID(busComboBox.getValue().getNormalSeats())-ticketDAO.countNormalSeats(busComboBox.getValue().getId())==0){;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Booking Error");
                    alert.setHeaderText("There are no Normal seats left");
                    alert.setContentText("Please choose another seat type");
                    alert.showAndWait();
                }
                // check if the seat is available?
                else if(!ticketDAO.isSeatAvailable(ticketSeatNumber.getText(),busComboBox.getValue().getId())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Booking Error");
                    alert.setHeaderText("Seat is not available");
                    alert.setContentText("Please choose another seat");
                    alert.showAndWait();
                }
                else {
                    TicketDAO ticketDAO = new TicketDAO();
                    Ticket ticket = ticketDAO.getByID(Integer.parseInt(ticketID.getText()));
                    ticket.setCustomer(customer);
                    ticket.setPurcharseTime(LocalDateTime.parse(ticketPurchaseTime.getText()));
                    ticket.setBus(busComboBox.getValue());
                    ticket.setSeatNumber((ticketSeatNumber.getText()));
                    ticket.setSeatType(ticketSeatType.getValue());
                    if(ticketSeatType.getValue().toString().equals("VIP")){
                        ticketPrice.setText(String.valueOf(busComboBox.getValue().getPrice() * 0.15+busComboBox.getValue().getPrice()));
                    }else {
                        ticketPrice.setText(String.valueOf(busComboBox.getValue().getPrice()));
                    }
                    ticket.setPrice(Float.parseFloat(ticketPrice.getText()));

                    int result =  ticketDAO.update(ticket);
                    // ALERT UPDATE SUCCESS
                    if(result !=0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Submit Success");
                        alert.setHeaderText("Update Success");
                        alert.setContentText("Update Success");
                        alert.showAndWait();
                    }
                    Stage stage = (Stage) btnSave.getScene().getWindow();
                    stage.close();
                }
            }
        });
        btnDelete.setOnAction(actionEven->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("DELETE CUSTOMER");
            alert.setContentText("Are you sure to delete this customer? This action cannot be undone");
            alert.showAndWait();
            if(alert.getResult().getText().equals("OK")){
                TicketDAO ticketDAO = new TicketDAO();
                Ticket ticket = ticketDAO.getByID(Integer.parseInt(ticketID.getText()));
                ticketDAO.deleteTicketByCustomer(ticket);
                CustomerDAO customerDAO = new CustomerDAO();
                Customer customer = customerDAO.getByID(Integer.parseInt(customerIDTextField.getText()));
                customerDAO.delete(customer);
                Stage stage = (Stage) btnDelete.getScene().getWindow();
                stage.close();
            }
        });
    }
    public void getCustomerInformation(DisplayInformation displayInformation){
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getByID(displayInformation.getCustomer_id());
        customerIDTextField.setText(String.valueOf(displayInformation.getCustomer_id()));
        customerIDTextField.setDisable(true);
        customerNameTextField.setText(customer.getName());
        customerNameTextField.setDisable(true);
        genderComboBox.setValue((Gender) customer.getGender());
        genderComboBox.setDisable(true);
        customerPhoneTextField.setText(customer.getPhone());
        customerPhoneTextField.setDisable(true);
        customerEmailTextField.setText(customer.getEmail());
        customerEmailTextField.setDisable(true);
        // Ticket Information
        TicketDAO ticketDAO = new TicketDAO();
        Ticket ticket = ticketDAO.getByID(displayInformation.getTicket_id());
        ticketID.setText(String.valueOf(ticket.getId()));
        ticketID.setDisable(true);
        customerIDTextField1.setText(String.valueOf(ticket.getCustomer().getId()));
        customerIDTextField1.setDisable(true);
        routeComboBox.setValue(new RouteDAO().getByID(ticket.getBus().getRoute().getId()));
        routeComboBox.setDisable(true);
        busComboBox.setValue(ticket.getBus());
        busComboBox.setDisable(true);
        ticketSeatNumber.setText(String.valueOf(ticket.getSeatNumber()));
        ticketSeatNumber.setDisable(true);
        ticketSeatType.setValue((SeatType) ticket.getSeatType());
        ticketSeatType.setDisable(true);
        ticketPrice.setText(String.valueOf(ticket.getPrice()));
        ticketPrice.setDisable(true);
        ticketPurchaseTime.setText(ticket.getPurcharseTime().toString());
        ticketPurchaseTime.setDisable(true);
    }
}
