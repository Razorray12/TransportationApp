package org.example.contoller;

import org.example.dao.CustomerDAO;
import org.example.model.Customer;
import org.bson.types.ObjectId;

import java.util.List;

public class CustomerController {
    private final CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    public List<Customer> getAllCustomersSorted(String sortField, boolean ascending) {
        return customerDAO.findAll(sortField, ascending);
    }

    public Customer getCustomerById(String id) {
        try {
            return customerDAO.findById(new ObjectId(id));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Customer> searchCustomersByName(String name) {
        return customerDAO.findByName(name);
    }

    public List<Customer> getCustomersByType(String customerType) {
        return customerDAO.findByType(customerType);
    }

    public boolean saveCustomer(Customer customer) {
        try {
            customerDAO.save(customer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(String id) {
        try {
            customerDAO.delete(new ObjectId(id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
