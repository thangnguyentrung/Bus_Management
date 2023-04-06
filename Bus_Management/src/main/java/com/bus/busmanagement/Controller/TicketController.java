package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.BusDAO;
import com.bus.busmanagement.DataAccess.CustomerDAO;
import com.bus.busmanagement.DataAccess.RouteDAO;
import com.bus.busmanagement.DataAccess.TicketDAO;
import com.bus.busmanagement.Entity.*;
import com.bus.busmanagement.MySQLConnection;
import com.bus.busmanagement.Validation.CustomerValidation;
import com.bus.busmanagement.Validation.TicketValidation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class TicketController implements Initializable {
    private RouteDAO routeDAO = new RouteDAO();
    private BusDAO busDAO = new BusDAO();
    private ObservableList<Route> routeList = routeDAO.getAll();
    @FXML
    private ComboBox<Route> routeComboBox;
    @FXML
    private ComboBox<Bus> busComboBox;
    @FXML
    private ComboBox<SeatType> seatTypeComboBox;
    private ObservableList<SeatType> seatTypeList = SeatType.getAll();
    @FXML
    private TextField seatNumberTextField;

    // Customer INFORMATION
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private ComboBox<Gender> genderComboBox;
    private ObservableList<Gender> genderList = Gender.getAll();
    // TICKET INFORMATION
    @FXML
    private Label nameTicketLabel;
    @FXML
    private Label phoneTicketLabel;
    @FXML
    private Label emailTicketLabel;
    @FXML
    private Label genderTicketLabel;
    @FXML
    private Label routeTicketLabel;
    @FXML
    private Label busTicketLabel;
    @FXML
    private Label seatTypeTicketLabel;
    @FXML
    private Label seatNumberTicketLabel;
    @FXML
    private Label priceTicketLabel;
    @FXML
    private Label customerIDTicketLabel;
    @FXML
    private Label busIDTicketLabel;
    private TicketDAO ticketDAO = new TicketDAO();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        routeComboBox.setItems(routeList);
        routeComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, route, t1) -> {
            busComboBox.setItems(busDAO.getByRoute(t1));
        });
        seatTypeComboBox.setItems(seatTypeList);
        genderComboBox.setItems(genderList);

        // BIDING TEXT FIELD, COMBOBOX TO LABEL
        nameTextField.textProperty().addListener((observableValue, s, name) -> {
            nameTicketLabel.setText(name);
        });
        genderComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, gender, gd) -> {
                    genderTicketLabel.setText(gd.toString());
                });
        phoneTextField.textProperty().addListener((observableValue, s, phone) -> {
            phoneTicketLabel.setText(phone);
        });
        emailTextField.textProperty().addListener((observableValue, s, email) -> {
            emailTicketLabel.setText(email);
        });
        routeComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, route, rt) -> {
                    routeTicketLabel.setText(rt.toString());
                });
        busComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, bus, bs) -> {
                    busTicketLabel.setText(bs.toString());
                });
        seatTypeComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, seatType, st) -> {
                    seatTypeTicketLabel.setText(st.toString());
                });
        seatNumberTextField.textProperty().addListener((observableValue, s, seatNumber) -> {
            seatNumberTicketLabel.setText(seatNumber);
        });

    }
    public void submitCustomerInformation(){
        //VALIDATE CUSTOMER INFORMATION
        if(nameTextField.getText().isEmpty() || phoneTextField.getText().isEmpty() || emailTextField.getText().isEmpty()|| genderComboBox.getValue()==null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Submit Error");
            alert.setHeaderText("There are some information missing");
            alert.setContentText("Please fill all information");
            alert.showAndWait();
        }else if(!CustomerValidation.isNameValid(nameTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Submit Error");
            alert.setHeaderText("Name is invalid");
            alert.setContentText("Please use only alphabet");
            alert.showAndWait();
        }else if(!CustomerValidation.isPhoneValid(phoneTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Submit Error");
            alert.setHeaderText("Phone is invalid");
            alert.setContentText("Please use only number\n atleast 10 digits");
            alert.showAndWait();
        }else if(!CustomerValidation.isEmailValid(emailTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Submit Error");
            alert.setHeaderText("Email is invalid");
            alert.setContentText("Please use correct email format");
            alert.showAndWait();
        }
        else {
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = new Customer();
            customer.setName(nameTextField.getText());
            customer.setGender(genderComboBox.getValue().toString());
            customer.setPhone(phoneTextField.getText());
            customer.setEmail(emailTextField.getText());

            int rs = customerDAO.add(customer);
            if(rs == -1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Submit Error");
                alert.setHeaderText("Phone or email has been existed");
                alert.setContentText("Please use another phone or email");
                alert.showAndWait();
            }else if(rs!=0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Submit Success");
                alert.setHeaderText("Customer information has been saved");
                alert.setContentText("Please continue to buy ticket");
                alert.showAndWait();
            }
            // Get Customer ID and Bus ID for ticket
            customerIDTicketLabel.setText(String.valueOf(customerDAO.getCustomerID(nameTextField.getText())));
            busIDTicketLabel.setText(String.valueOf(busComboBox.getValue().getId()));
            // Get Price for ticket, VIP rise 15%
            if(seatTypeComboBox.getValue().toString().equals("VIP")){
                priceTicketLabel.setText(String.valueOf(busComboBox.getValue().getPrice()+busComboBox.getValue().getPrice() * 0.15));
            }else {
                priceTicketLabel.setText(String.valueOf(busComboBox.getValue().getPrice()));
            }
        }



    }
    public void resetCustomerInformation(){
        nameTextField.setText("");
        phoneTextField.setText("");
        emailTextField.setText("");
        seatNumberTextField.setText("");
    }
    public void bookTicket(){
        //VALIDATE TICKET INFORMATION
        // check if there are some information missing?
        if(routeComboBox.getValue()==null || busComboBox.getValue()==null || seatTypeComboBox.getValue()==null || seatNumberTextField.getText().isEmpty()
                || nameTicketLabel.getText().isEmpty() || genderTicketLabel.getText().isEmpty()
                || phoneTicketLabel.getText().isEmpty() || emailTicketLabel.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText("There are some information missing");
            alert.setContentText("Please fill all information");
            alert.showAndWait();
        }
        // check if the VIP seat is full?
        else if(seatTypeComboBox.getValue().name().equals("VIP")&&busDAO.getVipSeatsByBusID(busComboBox.getValue().getVipSeats())-ticketDAO.countVIPSeats(busComboBox.getValue().getId())==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText("There are no VIP seats left");
            alert.setContentText("Please choose another seat type");
            alert.showAndWait();
        }
        // check if the Normal seat is full?
        else if(seatTypeComboBox.getValue().name().equals("NORMAL")&&busDAO.getNormalSeatsByBusID(busComboBox.getValue().getNormalSeats())-ticketDAO.countNormalSeats(busComboBox.getValue().getId())==0){;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText("There are no Normal seats left");
            alert.setContentText("Please choose another seat type");
            alert.showAndWait();
        }
        // check if the seat is available?
        else if(!ticketDAO.isSeatAvailable(seatNumberTextField.getText(),busComboBox.getValue().getId())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText("Seat is not available");
            alert.setContentText("Please choose another seat");
            alert.showAndWait();
        }
        else if(!TicketValidation.isSeatNumberValid(seatNumberTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText("Seat number is invalid");
            alert.setContentText("Please use number from 1 to 50\n character A to D\n" +
                    "example: 1A , 3B, ... 50D");
            alert.showAndWait();
        }else if(customerIDTicketLabel.getText().isEmpty() || busIDTicketLabel.getText().isEmpty()|| priceTicketLabel.getText().equals("$0.0")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText("Customer information is not submitted");
            alert.setContentText("You have to submit customer information first");
            alert.showAndWait();
        }
        else {
            // Save ticket to database
            Ticket ticket = new Ticket();
            ticket.setSeatNumber((seatNumberTextField.getText()));
            ticket.setSeatType(seatTypeComboBox.getValue());
            ticket.setPrice(Float.parseFloat(priceTicketLabel.getText()));
            ticket.setPurcharseTime(LocalDateTime.now());
            ticket.setCustomer(new CustomerDAO().getByID(Integer.parseInt(customerIDTicketLabel.getText())));
            ticket.setBus(new BusDAO().getByID(Integer.parseInt(busIDTicketLabel.getText())));
            int rs = ticketDAO.add(ticket);
            if(rs==-1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Booking Error");
                alert.setHeaderText("Seat number has been booked");
                alert.setContentText("Please use another seat number");
                alert.showAndWait();
            }
            // INFORM SUCCESS BOOKING
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Success");
            alert.setHeaderText("Ticket has been booked successfully");
            alert.setContentText("Check information in customer tab");
            alert.showAndWait();
            // RESET TICKET INFORMATION
            priceTicketLabel.setText("$0.0");
        }
    }
    public void getReceipt(){
        if(seatNumberTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Receipt Error");
            alert.setHeaderText("Seat number is missing");
            alert.setContentText("You have to book the ticket first");
            alert.showAndWait();
        }else {
            Ticket ticket = ticketDAO.getTicketBySeatNumberAndBusID(seatNumberTextField.getText(), Integer.parseInt(busIDTicketLabel.getText()));
            System.out.println(ticket.getId());
            Map<String, Object> params = new HashMap<>();
            params.put("ticket_id", ticket.getId());
            params.put("license_plate", ticket.getBus().getLicensePlate());
            params.put("start_date", ticket.getBus().getRoute().getStartDate());
            params.put("price", ticket.getPrice());
            params.put("name", ticket.getCustomer().getName());
            params.put("gender", ticket.getCustomer().getGender());
            params.put("phone", ticket.getCustomer().getPhone());
            params.put("route_name", ticket.getBus().getRoute().getName());
            params.put("bus_name", ticket.getBus().getName());
            params.put("seat_number", ticket.getSeatNumber());
            params.put("seat_type", ticket.getSeatType());
            params.put("purchase_time", ticket.getPurcharseTime());
            try {
                JasperReport jasperReport = JasperCompileManager.compileReport("D:\\Programing\\WorkSpace\\BusManagement\\src\\main\\resources\\com\\bus\\busmanagement\\Report\\bus-bill.jrxml");
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, MySQLConnection.getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                e.printStackTrace();
            }
        }

    }

}
