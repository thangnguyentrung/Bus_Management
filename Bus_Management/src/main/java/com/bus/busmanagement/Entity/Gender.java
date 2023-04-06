package com.bus.busmanagement.Entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;
    private static ObservableList<Gender> genders = FXCollections.observableArrayList(Gender.values());
    public static ObservableList<Gender> getAll(){
        return genders;
    }
}
