package org.example.contoller;

import org.example.dao.RouteDAO;
import org.example.model.Route;
import org.bson.types.ObjectId;

import java.util.List;

public class RouteController {
    private final RouteDAO routeDAO;

    public RouteController() {
        this.routeDAO = new RouteDAO();
    }

    public List<Route> getAllRoutes() {
        return routeDAO.findAll();
    }

    public List<Route> getAllRoutesSorted(String sortField, boolean ascending) {
        return routeDAO.findAll(sortField, ascending);
    }

    public Route getRouteById(String id) {
        try {
            return routeDAO.findById(new ObjectId(id));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Route> searchRoutesByName(String name) {
        return routeDAO.findByName(name);
    }

    public Route getRouteByEmployeeId(String employeeId) {
        try {
            return routeDAO.findByEmployeeId(new ObjectId(employeeId));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Route> getRoutesByStatus(String status) {
        return routeDAO.findByStatus(status);
    }

    public boolean saveRoute(Route route) {
        try {
            if (route.getAssignedEmployeeId() != null) {
                Route existingRoute = routeDAO.findByEmployeeId(route.getAssignedEmployeeId());
                if (existingRoute != null && !existingRoute.getId().equals(route.getId())) {
                    throw new IllegalStateException("Этот сотрудник уже назначен на другой маршрут");
                }
            }

            routeDAO.save(route);
            return true;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoute(String id) {
        try {
            routeDAO.delete(new ObjectId(id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void unassignEmployee(String employeeId) {
        try {
            routeDAO.unassignEmployee(new ObjectId(employeeId));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
