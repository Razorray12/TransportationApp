package org.example.model;

import org.bson.types.ObjectId;

public class Ship {
    private ObjectId id;
    private String name;
    private String type;
    private int capacity;
    private String registrationNumber;
    private int yearBuilt;
    private String currentLocation;
    private String status; // active, maintenance, inactive

    public Ship() {
    }

    public Ship(String name, String type, int capacity, String registrationNumber, int yearBuilt, String currentLocation, String status) {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.registrationNumber = registrationNumber;
        this.yearBuilt = yearBuilt;
        this.currentLocation = currentLocation;
        this.status = status;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(int yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " (" + registrationNumber + ")";
    }
}