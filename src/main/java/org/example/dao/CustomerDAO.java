package org.example.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.example.model.Customer;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.util.MongoDBConnection;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private final MongoCollection<Document> collection;

    public CustomerDAO() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("customers");
    }

    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        FindIterable<Document> documents = collection.find();

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                customers.add(documentToCustomer(doc));
            }
        }

        return customers;
    }

    public List<Customer> findAll(String sortField, boolean ascending) {
        List<Customer> customers = new ArrayList<>();
        FindIterable<Document> documents;

        if (ascending) {
            documents = collection.find().sort(Sorts.ascending(sortField));
        } else {
            documents = collection.find().sort(Sorts.descending(sortField));
        }

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                customers.add(documentToCustomer(doc));
            }
        }

        return customers;
    }

    public Customer findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToCustomer(doc) : null;
    }

    public List<Customer> findByName(String name) {
        List<Customer> customers = new ArrayList<>();
        FindIterable<Document> documents = collection.find(
                Filters.regex("name", ".*" + name + ".*", "i"));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                customers.add(documentToCustomer(doc));
            }
        }

        return customers;
    }

    public List<Customer> findByType(String customerType) {
        List<Customer> customers = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("customerType", customerType));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                customers.add(documentToCustomer(doc));
            }
        }

        return customers;
    }

    public ObjectId save(Customer customer) {
        Document doc = customerToDocument(customer);

        if (customer.getId() == null) {
            collection.insertOne(doc);
            customer.setId((ObjectId) doc.get("_id"));
        } else {
            collection.replaceOne(Filters.eq("_id", customer.getId()), doc);
        }

        return customer.getId();
    }

    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Customer documentToCustomer(Document doc) {
        Customer customer = new Customer();
        customer.setId(doc.getObjectId("_id"));
        customer.setName(doc.getString("name"));
        customer.setContactPerson(doc.getString("contactPerson"));
        customer.setEmail(doc.getString("email"));
        customer.setPhone(doc.getString("phone"));
        customer.setAddress(doc.getString("address"));
        customer.setRegistrationNumber(doc.getString("registrationNumber"));
        customer.setCustomerType(doc.getString("customerType"));
        customer.setNotes(doc.getString("notes"));

        return customer;
    }

    private Document customerToDocument(Customer customer) {
        Document doc = new Document();

        if (customer.getId() != null) {
            doc.append("_id", customer.getId());
        }

        doc.append("name", customer.getName())
                .append("contactPerson", customer.getContactPerson())
                .append("email", customer.getEmail())
                .append("phone", customer.getPhone())
                .append("address", customer.getAddress())
                .append("registrationNumber", customer.getRegistrationNumber())
                .append("customerType", customer.getCustomerType())
                .append("notes", customer.getNotes());

        return doc;
    }
}