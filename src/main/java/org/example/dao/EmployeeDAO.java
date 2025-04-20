package org.example.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import org.example.model.Employee;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.util.MongoDBConnection;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private final MongoCollection<Document> collection;

    public EmployeeDAO() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("employees");
    }

    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        FindIterable<Document> documents = collection.find();

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                employees.add(documentToEmployee(doc));
            }
        }

        return employees;
    }

    public List<Employee> findAll(String sortField, boolean ascending) {
        List<Employee> employees = new ArrayList<>();
        FindIterable<Document> documents;

        if (ascending) {
            documents = collection.find().sort(Sorts.ascending(sortField));
        } else {
            documents = collection.find().sort(Sorts.descending(sortField));
        }

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                employees.add(documentToEmployee(doc));
            }
        }

        return employees;
    }

    public Employee findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToEmployee(doc) : null;
    }

    public List<Employee> findByName(String name) {
        List<Employee> employees = new ArrayList<>();
        FindIterable<Document> documents = collection.find(
                Filters.or(
                        Filters.regex("firstName", ".*" + name + ".*", "i"),
                        Filters.regex("lastName", ".*" + name + ".*", "i")
                ));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                employees.add(documentToEmployee(doc));
            }
        }

        return employees;
    }

    public List<Employee> findByPosition(String position) {
        List<Employee> employees = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("position", position));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                employees.add(documentToEmployee(doc));
            }
        }

        return employees;
    }

    public List<Employee> findByStatus(String status) {
        List<Employee> employees = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("status", status));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                employees.add(documentToEmployee(doc));
            }
        }

        return employees;
    }

    public ObjectId save(Employee employee) {
        Document doc = employeeToDocument(employee);

        if (employee.getId() == null) {
            collection.insertOne(doc);
            employee.setId((ObjectId) doc.get("_id"));
        } else {
            collection.replaceOne(Filters.eq("_id", employee.getId()), doc);
        }

        return employee.getId();
    }

    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Employee documentToEmployee(Document doc) {
        Employee employee = new Employee();
        employee.setId(doc.getObjectId("_id"));
        employee.setFirstName(doc.getString("firstName"));
        employee.setLastName(doc.getString("lastName"));
        employee.setPosition(doc.getString("position"));
        employee.setHireDate(doc.getDate("hireDate"));
        employee.setEmail(doc.getString("email"));
        employee.setPhone(doc.getString("phone"));
        employee.setAddress(doc.getString("address"));
        employee.setPassportNumber(doc.getString("passportNumber"));
        employee.setStatus(doc.getString("status"));

        return employee;
    }

    private Document employeeToDocument(Employee employee) {
        Document doc = new Document();

        if (employee.getId() != null) {
            doc.append("_id", employee.getId());
        }

        doc.append("firstName", employee.getFirstName())
                .append("lastName", employee.getLastName())
                .append("position", employee.getPosition())
                .append("hireDate", employee.getHireDate())
                .append("email", employee.getEmail())
                .append("phone", employee.getPhone())
                .append("address", employee.getAddress())
                .append("passportNumber", employee.getPassportNumber())
                .append("status", employee.getStatus());

        return doc;
    }
}