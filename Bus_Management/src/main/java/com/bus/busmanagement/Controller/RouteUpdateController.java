package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.RouteDAO;
import com.bus.busmanagement.Entity.Route;
import com.bus.busmanagement.Validation.RouteValidation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RouteUpdateController implements Initializable {
    @FXML
    TextField routeID;
    @FXML
    TextField routeName;
    @FXML
    TextField routeOrigin;
    @FXML
    TextField routeDestination;
    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;
    @FXML
    Button btnSave;
    private Stage stage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    btnSave.setOnAction(e->{
        String routeIDText = routeID.getText();
        String routeNameText = routeName.getText();
        String routeOriginText = routeOrigin.getText();
        String routeDestinationText = routeDestination.getText();
        LocalDate startDateText = startDate.getValue();
        LocalDate endDateText = endDate.getValue();
        // VALIDATION
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
            Route newRoute = new Route(Integer.parseInt(routeIDText),routeNameText,routeOriginText,routeDestinationText,startDateText,endDateText);
            RouteDAO routeDAO = new RouteDAO();
            routeDAO.update(newRoute);

            stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }
    });
    }
    public void getRoute(Route route){
        routeID.setText(String.valueOf(route.getId()));
        routeID.setDisable(true);
        routeName.setText(route.getName());
        routeOrigin.setText(route.getOrigin());
        routeDestination.setText(route.getDestination());
        startDate.setValue(route.getStartDate());
        endDate.setValue(route.getEndDate());
    }
}
