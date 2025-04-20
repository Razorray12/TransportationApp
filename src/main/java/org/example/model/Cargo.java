package org.example.model;

import org.bson.types.ObjectId;
import java.util.Date;

public class Cargo {
    private ObjectId id;
    private String description;
    private double weight;
    private String type;
    private ObjectId shipId; // Связь с кораблем (один-ко-многим)
    private ObjectId customerId; // Связь с клиентом
    private String originPort;
    private String destinationPort;
    private Date loadingDate;
    private Date deliveryDate;
    private String status; // loading, in transit, delivered

    public Cargo() {
    }

    public Cargo(String description, double weight, String type, ObjectId shipId, ObjectId customerId,
                 String originPort, String destinationPort, Date loadingDate, Date deliveryDate, String status) {
        this.description = description;
        this.weight = weight;
        this.type = type;
        this.shipId = shipId;
        this.customerId = customerId;
        this.originPort = originPort;
        this.destinationPort = destinationPort;
        this.loadingDate = loadingDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ObjectId getShipId() {
        return shipId;
    }

    public void setShipId(ObjectId shipId) {
        this.shipId = shipId;
    }

    public ObjectId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    public String getOriginPort() {
        return originPort;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public Date getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(Date loadingDate) {
        this.loadingDate = loadingDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return description + " (" + weight + " тонн)";
    }
}