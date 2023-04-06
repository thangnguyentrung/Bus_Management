package com.bus.busmanagement.Validation;

public class SignUpValidation {
    private static final String USERNAME_PATTERN =
            "^[a-zA-Z0-9._-]{3,20}$";
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{6,20}$";
    // check password phải có ít nhất 6 kí tự
    // , chứa cả chữ cả số, ít nhất 1 kí tự viết hoa, 1 kí tự đặc biệt trong java
    public static boolean isUsernameValid(String username){
        return username.matches(USERNAME_PATTERN);
    }
    public static boolean isPasswordValid(String password){
        return password.matches(PASSWORD_PATTERN);
    }
    public static boolean isConfirmPasswordValid(String password,String confirmPassword){
        return password.equals(confirmPassword);
    }
}
