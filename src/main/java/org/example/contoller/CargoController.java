package org.example.contoller;

import org.example.dao.CargoDAO;
import org.example.model.Cargo;
import org.bson.types.ObjectId;

import java.util.List;

public class CargoController {
    private final CargoDAO cargoDAO;

    public CargoController() {
        this.cargoDAO = new CargoDAO();
    }

    public List<Cargo> getAllCargos() {
        return cargoDAO.findAll();
    }

    public List<Cargo> getAllCargosSorted(String sortField, boolean ascending) {
        return cargoDAO.findAll(sortField, ascending);
    }

    public Cargo getCargoById(String id) {
        try {
            return cargoDAO.findById(new ObjectId(id));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Cargo> searchCargosByDescription(String description) {
        return cargoDAO.findByDescription(description);
    }

    public List<Cargo> getCargosByShipId(String shipId) {
        try {
            return cargoDAO.findByShipId(new ObjectId(shipId));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Cargo> getCargosByCustomerId(String customerId) {
        try {
            return cargoDAO.findByCustomerId(new ObjectId(customerId));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Cargo> getCargosByStatus(String status) {
        return cargoDAO.findByStatus(status);
    }

    public boolean saveCargo(Cargo cargo) {
        try {
            cargoDAO.save(cargo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCargo(String id) {
        try {
            cargoDAO.delete(new ObjectId(id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
