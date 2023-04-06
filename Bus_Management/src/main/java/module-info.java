module com.bus.busmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.themify;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires MaterialFX;
    requires java.sql;
    requires bcrypt;
    requires jasperreports;

    opens com.bus.busmanagement to javafx.fxml;
    exports com.bus.busmanagement;
    exports com.bus.busmanagement.Controller;
    opens com.bus.busmanagement.Controller to javafx.fxml;
    opens com.bus.busmanagement.Buses to javafx.fxml;
    opens com.bus.busmanagement.Entity to javafx.base;


}