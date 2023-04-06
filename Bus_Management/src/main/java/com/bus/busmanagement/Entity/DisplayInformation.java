package com.bus.busmanagement.Entity;

import java.time.LocalDateTime;

public class DisplayInformation {
    private int customer_id;
    private int ticket_id;
    private String fullName;
    private String gender;
    private String phone;
    private String Route;
    private String busName;
    private String seatNumber;
    private String seatType;
    private LocalDateTime purchaseTime;
    public DisplayInformation() {
    }
    public DisplayInformation(int customer_id, int ticket_id, String fullName,
                              String gender, String phone, String route,
                              String busName, String seatNumber, String seatType,
                              LocalDateTime purchaseTime) {
        this.customer_id = customer_id;
        this.ticket_id = ticket_id;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        Route = route;
        this.busName = busName;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.purchaseTime = purchaseTime;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

}
