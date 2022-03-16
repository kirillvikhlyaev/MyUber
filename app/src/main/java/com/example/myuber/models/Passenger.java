package com.example.myuber.models;

public class Passenger {
    private String passengerID;
    private String name;
    private String email;

    public Passenger() {
    }

    public Passenger(String passengerID, String name, String email) {
        this.passengerID = passengerID;
        this.name = name;
        this.email = email;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
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
