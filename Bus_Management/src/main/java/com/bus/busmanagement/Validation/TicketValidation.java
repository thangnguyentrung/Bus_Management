package com.bus.busmanagement.Validation;

public class TicketValidation {
    private static String seatNumberPattern = "^([1-9]|[1-4][0-9]|50)[ABCD]$";
    public static boolean isSeatNumberValid(String seatNumber) {
        return seatNumber.matches(seatNumberPattern);
    }
}
