package org.example.contoller;

import org.example.dao.EmployeeDAO;
import org.example.dao.RouteDAO;
import org.example.model.Employee;
import org.bson.types.ObjectId;
import org.example.model.Route;

import java.util.List;

public class EmployeeController {
    private final EmployeeDAO employeeDAO;
    private final RouteDAO routeDAO;

    public EmployeeController() {
        this.employeeDAO = new EmployeeDAO();
        this.routeDAO = new RouteDAO();
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    public List<Employee> getAllEmployeesSorted(String sortField, boolean ascending) {
        return employeeDAO.findAll(sortField, ascending);
    }

    public Employee getEmployeeById(String id) {
        try {
            return employeeDAO.findById(new ObjectId(id));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Employee> searchEmployeesByName(String name) {
        return employeeDAO.findByName(name);
    }

    public List<Employee> getEmployeesByPosition(String position) {
        return employeeDAO.findByPosition(position);
    }

    public List<Employee> getEmployeesByStatus(String status) {
        return employeeDAO.findByStatus(status);
    }

    public boolean saveEmployee(Employee employee) {
        try {
            employeeDAO.save(employee);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmployee(String id) {
        try {
            ObjectId employeeId = new ObjectId(id);

            Route assignedRoute = routeDAO.findByEmployeeId(employeeId);

            if (assignedRoute != null) {
                assignedRoute.setAssignedEmployeeId(null);
                routeDAO.save(assignedRoute);
            }

            employeeDAO.delete(employeeId);
            return true;

        } catch (IllegalArgumentException e) {
            System.err.println("Некорректный ID сотрудника: " + id);
            return false;
        } catch (Exception e) {
            System.err.println("Ошибка при удалении сотрудника: " + e.getMessage());
            return false;
        }
    }
}
