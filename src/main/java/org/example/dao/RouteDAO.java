package org.example.dao;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import org.example.model.Route;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.util.MongoDBConnection;

import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    private final MongoCollection<Document> collection;

    public RouteDAO() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("routes");
    }

    public List<Route> findAll() {
        List<Route> routes = new ArrayList<>();
        FindIterable<Document> documents = collection.find();

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                routes.add(documentToRoute(doc));
            }
        }

        return routes;
    }

    public List<Route> findAll(String sortField, boolean ascending) {
        List<Route> routes = new ArrayList<>();
        FindIterable<Document> documents;

        if (ascending) {
            documents = collection.find().sort(Sorts.ascending(sortField));
        } else {
            documents = collection.find().sort(Sorts.descending(sortField));
        }

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                routes.add(documentToRoute(doc));
            }
        }

        return routes;
    }

    public Route findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToRoute(doc) : null;
    }

    public List<Route> findByName(String name) {
        List<Route> routes = new ArrayList<>();
        FindIterable<Document> documents = collection.find(
                Filters.regex("name", ".*" + name + ".*", "i"));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                routes.add(documentToRoute(doc));
            }
        }

        return routes;
    }

    public Route findByEmployeeId(ObjectId employeeId) {
        Document doc = collection.find(Filters.eq("assignedEmployeeId", employeeId)).first();
        return doc != null ? documentToRoute(doc) : null;
    }

    public List<Route> findByStatus(String status) {
        List<Route> routes = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("status", status));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                routes.add(documentToRoute(doc));
            }
        }

        return routes;
    }

    public ObjectId save(Route route) {
        Document doc = routeToDocument(route);

        if (route.getId() == null) {
            collection.insertOne(doc);
            route.setId((ObjectId) doc.get("_id"));
        } else {
            collection.replaceOne(Filters.eq("_id", route.getId()), doc);
        }

        return route.getId();
    }

    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Route documentToRoute(Document doc) {
        Route route = new Route();
        route.setId(doc.getObjectId("_id"));
        route.setName(doc.getString("name"));
        route.setStartPort(doc.getString("startPort"));
        route.setEndPort(doc.getString("endPort"));
        route.setDistance(doc.getDouble("distance"));
        route.setEstimatedDays(doc.getInteger("estimatedDays"));
        route.setAssignedEmployeeId(doc.getObjectId("assignedEmployeeId"));
        route.setDescription(doc.getString("description"));
        route.setStatus(doc.getString("status"));

        return route;
    }

    private Document routeToDocument(Route route) {
        Document doc = new Document();

        if (route.getId() != null) {
            doc.append("_id", route.getId());
        }

        doc.append("name", route.getName())
                .append("startPort", route.getStartPort())
                .append("endPort", route.getEndPort())
                .append("distance", route.getDistance())
                .append("estimatedDays", route.getEstimatedDays())
                .append("assignedEmployeeId", route.getAssignedEmployeeId())
                .append("description", route.getDescription())
                .append("status", route.getStatus());

        return doc;
    }
}