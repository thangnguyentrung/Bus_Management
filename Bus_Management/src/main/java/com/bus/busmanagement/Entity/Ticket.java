package com.bus.busmanagement.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private String seatNumber;
    private Enum<SeatType> seatType;
    private Float price;
    private LocalDateTime purcharseTime;
    private Bus bus;
    private Customer customer;
    public Ticket() {
    }

    public Ticket(int id, String seatNumber, Enum<SeatType> seatType, Float price, LocalDateTime purcharseTime, Bus bus, Customer customer) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
        this.purcharseTime = purcharseTime;
        this.bus = bus;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Enum<SeatType> getSeatType() {
        return seatType;
    }

    public void setSeatType(Enum<SeatType> seatType) {
        this.seatType = seatType;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDateTime getPurcharseTime() {
        return purcharseTime;
    }

    public void setPurcharseTime(LocalDateTime purcharseTime) {
        this.purcharseTime = purcharseTime;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
