package com.bus.busmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class FxmlLoader {
    private AnchorPane view;

    public AnchorPane getPage(String fileName) {
        try {
            view = FXMLLoader.load((getClass().getResource(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }
}
