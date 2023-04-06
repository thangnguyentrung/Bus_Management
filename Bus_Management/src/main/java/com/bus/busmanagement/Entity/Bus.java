package com.bus.busmanagement.Entity;

public class Bus {
    private int id;
    private String name;
    private String licensePlate;
    private int capacity;
    private int vipSeats;
    private float price;
    private Route route;
    private Enum<BusStatus> status;
    public Bus() {
    }

    public Bus(int id, String name, String licensePlate, int capacity, int VIPSeats,
               float price, Route route, Enum<BusStatus> status) {
        this.id = id;
        this.name = name;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.vipSeats = VIPSeats;
        this.price = price;
        this.route = route;
        this.status = status;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getVipSeats() {
        return vipSeats;
    }

    public void setVipSeats(int vipSeats) {
        this.vipSeats = vipSeats;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Enum<BusStatus> getStatus() {
        return status;
    }

    public void setStatus(String status) {
       this.status = BusStatus.valueOf(status);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getNormalSeats() {
        return this.capacity - this.vipSeats;
    }
}
