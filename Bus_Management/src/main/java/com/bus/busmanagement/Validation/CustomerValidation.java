package com.bus.busmanagement.Validation;

public class CustomerValidation {
    private static String namePattern = "^[a-zA-Z\\s]+$";
    private static String phonePattern = "^\\+?[0-9]{10,}$";
    private static String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static boolean isNameValid(String name) {
        return name.matches(namePattern);
    }
    public static boolean isPhoneValid(String phone) {
        return phone.matches(phonePattern);
    }
    public static boolean isEmailValid(String email) {
        return email.matches(emailPattern);
    }
}
