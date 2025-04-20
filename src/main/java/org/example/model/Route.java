package org.example.model;

import org.bson.types.ObjectId;
import java.util.Date;

public class Route {
    private ObjectId id;
    private String name;
    private String startPort;
    private String endPort;
    private double distance;
    private int estimatedDays;
    private ObjectId assignedEmployeeId; // Связь один-к-одному с сотрудником
    private String description;
    private String status; // active, inactive

    public Route() {
    }

    public Route(String name, String startPort, String endPort, double distance, int estimatedDays,
                 ObjectId assignedEmployeeId, String description, String status) {
        this.name = name;
        this.startPort = startPort;
        this.endPort = endPort;
        this.distance = distance;
        this.estimatedDays = estimatedDays;
        this.assignedEmployeeId = assignedEmployeeId;
        this.description = description;
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

    public String getStartPort() {
        return startPort;
    }

    public void setStartPort(String startPort) {
        this.startPort = startPort;
    }

    public String getEndPort() {
        return endPort;
    }

    public void setEndPort(String endPort) {
        this.endPort = endPort;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public ObjectId getAssignedEmployeeId() {
        return assignedEmployeeId;
    }

    public void setAssignedEmployeeId(ObjectId assignedEmployeeId) {
        this.assignedEmployeeId = assignedEmployeeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " (" + startPort + " -> " + endPort + ")";
    }
}