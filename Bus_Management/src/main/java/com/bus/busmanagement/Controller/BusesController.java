package com.bus.busmanagement.Controller;

import com.bus.busmanagement.DataAccess.BusDAO;
import com.bus.busmanagement.DataAccess.RouteDAO;
import com.bus.busmanagement.DataAccess.TicketDAO;
import com.bus.busmanagement.Entity.Bus;
import com.bus.busmanagement.Entity.BusStatus;
import com.bus.busmanagement.Entity.Route;
import com.bus.busmanagement.FxmlLoader;
import com.bus.busmanagement.Validation.BusValidation;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class BusesController implements Initializable {
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
    private ComboBox<Route> routeComboBox;
    private ObservableList<Route> routes;
    private ObservableList<Bus> bus;
    @FXML
    private TableView<Bus> busTableView;
    @FXML
    private TableColumn<Bus, Integer> busIDColumn;
    @FXML
    private TableColumn<Bus, String> busNameColumn;
    @FXML
    private TableColumn<Bus, String> busLicensePlateColumn;
    @FXML
    private TableColumn<Bus, Integer> busCapacityColumn;
    @FXML
    private TableColumn<Bus, Integer> busVIPSeatsColumn;
    @FXML
    private TableColumn<Bus, Float> busPriceColumn;
    @FXML
    private TableColumn<Bus, String> busRouteColumn;
    @FXML
    private TableColumn<Bus, String> busStatusColumn;
    @FXML
    private FontIcon btnSearch;
    @FXML
    private TextField searchField;
    @FXML
    private FontIcon refreshIcon;
    private BusDAO busDAO = new BusDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    public void search(String keyword){
        if(keyword.isEmpty()){
            busTableView.setItems(bus);
        }else {
            ObservableList<Bus> searchList = busDAO.searchByKeyWord(keyword);
            busTableView.setItems(searchList);
        }
    }
    public void refreshTable(){
        busTableView.setItems(busDAO.getAll());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RouteDAO routeDAO = new RouteDAO();
        routes = routeDAO.getAll();
        routeComboBox.setItems(routes);

        bus = busDAO.getAll();
        // display data in table
        busIDColumn.setCellValueFactory(new PropertyValueFactory<Bus,Integer>("id"));
        busNameColumn.setCellValueFactory(new PropertyValueFactory<Bus,String>("name"));
        busLicensePlateColumn.setCellValueFactory(new PropertyValueFactory<Bus,String>("licensePlate"));
        busCapacityColumn.setCellValueFactory(new PropertyValueFactory<Bus,Integer>("capacity"));
        busVIPSeatsColumn.setCellValueFactory(new PropertyValueFactory<Bus,Integer>("vipSeats"));
        busRouteColumn.setCellValueFactory(new PropertyValueFactory<Bus,String>("route"));
        busStatusColumn.setCellValueFactory(new PropertyValueFactory<Bus,String>("status"));
        busPriceColumn.setCellValueFactory(new PropertyValueFactory<Bus,Float>("price"));
        // FORMAT PRICE
        busPriceColumn.setCellFactory(tc -> new TableCell<Bus, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Locale localeVN = new Locale("vi", "VN");
                    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
                    setText(currencyVN.format(item));
                }
            }
        });
        busTableView.setItems(bus);
        //SEARCH FUNCTION
        btnSearch.setOnMouseClicked(mouseEvent -> {
            search(searchField.getText());
        });
        searchField.setOnKeyReleased(keyEvent -> {
            search(searchField.getText());
        });
        //REFRESH TABLE
        refreshIcon.setOnMouseClicked(mouseEvent -> {
            refreshTable();
        });
        // UPDATE BUS STATUS WHEN BUS IS FULL
        bus.forEach(b -> {
            if(ticketDAO.countSeats(b.getId()) == b.getCapacity()){
                b.setStatus(BusStatus.FULL.name());
                busDAO.update(b);
            }
        });
        busTableView.setItems(busDAO.getAll());
    }
    public void addBus() {
        // VALIDATE DATA
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
            alert.setContentText("License plate must be in the format ABC-2312,A2D-1234");
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
            Bus bus = new Bus();
            bus.setName(busName.getText());
            bus.setLicensePlate(licensePlate.getText());
            bus.setCapacity(Integer.parseInt(capacity.getText()));
            bus.setVipSeats(Integer.parseInt(VIPSeats.getText()));
            bus.setPrice(Float.parseFloat(price.getText()));
            bus.setRoute(routeComboBox.getValue());
            bus.setStatus(BusStatus.AVAILABLE.toString());
            BusDAO busDAO = new BusDAO();
            int rs = busDAO.add(bus);
            if (rs!=0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Bus added successfully");
                alert.showAndWait();
                busTableView.getItems().add(bus);
                //refresh text fields
                busName.setText("");
                licensePlate.setText("");
                capacity.setText("");
                VIPSeats.setText("");
                routeComboBox.setValue(null);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error adding bus");
                alert.setContentText("Please try again");
                alert.showAndWait();
            }
        }
    }
    public void deleteBus() {
        Bus bus = busTableView.getSelectionModel().getSelectedItem();
        if(bus == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No bus selected");
            alert.setContentText("Please select a bus to delete");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete bus");
        alert.setHeaderText("Are you sure you want to delete this bus?");
        alert.setContentText("This action cannot be undone");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            BusDAO busDAO = new BusDAO();
            int rs = busDAO.delete(bus);
            if(rs == -1){
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText("Delete failed");
                alert1.setContentText("This bus is currently in use by the other entities ");
                alert1.showAndWait();
            }
        }
    }
    public void updateBus(){
        Bus bus = busTableView.getSelectionModel().getSelectedItem();
        if(bus == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No bus selected");
            alert.setContentText("Please select a bus to update");
            alert.showAndWait();
            return;
        }
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bus/busmanagement/Buses/Buses-Update.fxml"));
        try{
            AnchorPane viewUpdateBus = loader.load();
            Scene scene = new Scene(viewUpdateBus);
            BusesUpdateController busesUpdateController = loader.getController();
            busesUpdateController.getBus(bus);
            stage.setTitle("Update Route");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void resetFields() {
        busName.setText("");
        licensePlate.setText("");
        capacity.setText("");
        VIPSeats.setText("");
        routeComboBox.setValue(null);
    }
}
