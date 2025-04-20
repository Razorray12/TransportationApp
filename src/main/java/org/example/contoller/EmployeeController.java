package org.example.contoller;

import org.example.dao.EmployeeDAO;
import org.example.model.Employee;
import org.bson.types.ObjectId;

import java.util.List;

public class EmployeeController {
    private final EmployeeDAO employeeDAO;

    public EmployeeController() {
        this.employeeDAO = new EmployeeDAO();
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
            employeeDAO.delete(new ObjectId(id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
