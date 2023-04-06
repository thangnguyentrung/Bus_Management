package com.bus.busmanagement.Validation;

public class RouteValidation {
    public static boolean isValidRoute(String route){
       return route.matches("^[\\w\\s-]+$");
    }
    public static boolean isValidOriginOrDestination(String originOrDestination){
        return originOrDestination.matches("^[\\p{L}0-9/\\s-]+$");
    }
}
