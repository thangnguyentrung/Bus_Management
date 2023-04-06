package com.bus.busmanagement.Entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum SeatType {
    VIP,
    NORMAL;
    private static ObservableList<SeatType> seatTypes = FXCollections.observableArrayList(SeatType.values());
    public static ObservableList<SeatType> getAll(){
        return seatTypes;
    }
}
