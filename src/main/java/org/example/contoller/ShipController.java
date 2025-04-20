package org.example.contoller;

import org.example.dao.ShipDAO;
import org.example.model.Ship;
import org.bson.types.ObjectId;

import java.util.List;

public class ShipController {
    private final ShipDAO shipDAO;

    public ShipController() {
        this.shipDAO = new ShipDAO();
    }

    public List<Ship> getAllShips() {
        return shipDAO.findAll();
    }

    public List<Ship> getAllShipsSorted(String sortField, boolean ascending) {
        return shipDAO.findAll(sortField, ascending);
    }

    public Ship getShipById(String id) {
        try {
            return shipDAO.findById(new ObjectId(id));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Ship> searchShipsByName(String name) {
        return shipDAO.findByName(name);
    }

    public List<Ship> getShipsByType(String type) {
        return shipDAO.findByType(type);
    }

    public List<Ship> getShipsByStatus(String status) {
        return shipDAO.findByStatus(status);
    }

    public boolean saveShip(Ship ship) {
        try {
            shipDAO.save(ship);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteShip(String id) {
        try {
            shipDAO.delete(new ObjectId(id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}