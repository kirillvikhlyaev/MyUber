package com.example.myuber.models;

public class Driver {
    private String driverID;
    private String name;
    private String email;

    public Driver() {
    }

    public Driver(String driverID, String name, String email) {
        this.driverID = driverID;
        this.name = name;
        this.email = email;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
