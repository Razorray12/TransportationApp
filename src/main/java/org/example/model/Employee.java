package org.example.model;

import org.bson.types.ObjectId;
import java.util.Date;

public class Employee {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String position;
    private Date hireDate;
    private String email;
    private String phone;
    private String address;
    private String passportNumber;
    private String status; // active, on leave, terminated

    public Employee() {
    }

    public Employee(String firstName, String lastName, String position, Date hireDate, String email,
                    String phone, String address, String passportNumber, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.hireDate = hireDate;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.passportNumber = passportNumber;
        this.status = status;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + position + ")";
    }
}