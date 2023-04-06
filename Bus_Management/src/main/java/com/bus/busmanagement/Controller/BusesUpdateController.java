package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.BusDAO;
import com.bus.busmanagement.DataAccess.RouteDAO;
import com.bus.busmanagement.Entity.Bus;
import com.bus.busmanagement.Entity.BusStatus;
import com.bus.busmanagement.Entity.Route;
import com.bus.busmanagement.Validation.BusValidation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.net.URL;
import java.util.ResourceBundle;

public class BusesUpdateController implements Initializable {
   @FXML
   TextField busID;
   @FXML
    TextField busName;
    @FXML
    TextField licensePlate;
    @FXML
    TextField capacity;
    @FXML
    TextField VIPSeats;
    @FXML
    TextField price;
    @FXML
   ComboBox<Route> routeComboBox;
    @FXML
    Button btnSave;
    private Stage stage;

   public void getBus(Bus bus) {
         busID.setText(String.valueOf(bus.getId()));
         busID.setDisable(true);
         busName.setText(bus.getName());
         licensePlate.setText(bus.getLicensePlate());
         capacity.setText(String.valueOf(bus.getCapacity()));
         VIPSeats.setText(String.valueOf(bus.getVipSeats()));
         price.setText(String.valueOf(bus.getPrice()));
         routeComboBox.setValue(bus.getRoute());
   }

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
       RouteDAO routeDAO = new RouteDAO();
       routeComboBox.setItems(routeDAO.getAll());
       btnSave.setOnAction(e->{
           // VALIDATION
           if(busName.getText().isEmpty() || licensePlate.getText().isEmpty() || capacity.getText().isEmpty() || VIPSeats.getText().isEmpty() || price.getText().isEmpty() || routeComboBox.getValue() == null){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText("Missing data");
               alert.setContentText("Please fill in all the fields");
               alert.showAndWait();
           } else if (!BusValidation.isBusNameValid(busName.getText())) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText("Invalid bus name");
               alert.setContentText("Bus name must not contain special\n" +
                       "no space characters at the beginning or end");
               alert.showAndWait();
           } else if (!BusValidation.isLicensePlateValid(licensePlate.getText())) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText("Invalid license plate");
               alert.setContentText("License plate must be in the format ABC-1234");
               alert.showAndWait();
           } else if (!BusValidation.isCapacityValid(capacity.getText())) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText("Invalid capacity");
               alert.setContentText("Capacity must be a positive integer");
               alert.showAndWait();
           } else if (!BusValidation.isVIPSeatsValid(VIPSeats.getText(),capacity.getText())) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText("Invalid VIP seats");
               alert.setContentText("VIP seats must be a positive integer and must be less than or equal to capacity");
               alert.showAndWait();
           } else if (!BusValidation.isPriceValid(price.getText())) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText("Invalid price");
               alert.setContentText("Price must be a positive float");
               alert.showAndWait();
           }else {
               String busIDText = busID.getText();
               String busNameText = busName.getText();
               String licensePlateText = licensePlate.getText();
               String capacityText = capacity.getText();
               String VIPSeatsText = VIPSeats.getText();
               String priceText = price.getText();
               Route route = routeComboBox.getValue();

               Bus newBus = new Bus(Integer.parseInt(busIDText),
                       busNameText,licensePlateText,Integer.parseInt(capacityText)
                       ,Integer.parseInt(VIPSeatsText),Float.parseFloat(priceText),route, BusStatus.AVAILABLE);
               BusDAO busDAO = new BusDAO();
               int rs = busDAO.update(newBus);
               if(rs !=0){
                     Alert alert = new Alert(Alert.AlertType.INFORMATION);
                     alert.setTitle("Success");
                     alert.setHeaderText("Update bus successfully");
                     alert.showAndWait();

                   Stage stage = (Stage) btnSave.getScene().getWindow();
                   stage.close();
               }
           }
       });
   }
}
