package com.bus.busmanagement.Validation;

import com.bus.busmanagement.Entity.Route;

public class BusValidation {
    public static boolean isBusNameValid(String busName) {
        return busName.matches("^[a-zA-Z0-9][a-zA-Z0-9\\s]*[a-zA-Z0-9]$");
    }
    public static boolean isLicensePlateValid(String licensePlate) {
        return licensePlate.matches("^[A-Z0-9]{3}-[0-9]{3,4}$");
    }
    public static boolean isCapacityValid(String capacity) {
        try {
            int capacityInt = Integer.parseInt(capacity);
            return capacityInt > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isVIPSeatsValid(String VIPSeats,String capacity) {
        try {
            int VIPSeatsInt = Integer.parseInt(VIPSeats);
            int capacityInt = Integer.parseInt(capacity);
            return VIPSeatsInt >= 0 && VIPSeatsInt <= capacityInt;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isPriceValid(String price) {
        try {
            float priceFloat = Float.parseFloat(price);
            return priceFloat > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
