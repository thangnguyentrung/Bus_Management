package com.bus.busmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BusManagementApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BusManagementApp.class.getResource("Login/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String cssFile = BusManagementApp.class.getResource("CSS/Dashboard.css").toExternalForm();
        scene.getStylesheets().add(cssFile);
        stage.setTitle("Hyper Bus System");
        stage.setScene(scene);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}