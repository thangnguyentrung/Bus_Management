package com.bus.busmanagement.Entity;

import javafx.collections.ObservableList;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String email;
    private Enum<Gender> gender;
    public Customer() {
    }
    public Customer(int id, String name, String phone, String email, Enum<Gender> gender) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Enum<Gender> getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender);
    }

}
