package org.example.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import org.example.model.Ship;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.util.MongoDBConnection;

import java.util.ArrayList;
import java.util.List;

public class ShipDAO {
    private final MongoCollection<Document> collection;

    public ShipDAO() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("ships");
    }

    public List<Ship> findAll() {
        List<Ship> ships = new ArrayList<>();
        FindIterable<Document> documents = collection.find();

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ships.add(documentToShip(doc));
            }
        }

        return ships;
    }

    public List<Ship> findAll(String sortField, boolean ascending) {
        List<Ship> ships = new ArrayList<>();
        FindIterable<Document> documents;

        if (ascending) {
            documents = collection.find().sort(Sorts.ascending(sortField));
        } else {
            documents = collection.find().sort(Sorts.descending(sortField));
        }

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ships.add(documentToShip(doc));
            }
        }

        return ships;
    }

    public Ship findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToShip(doc) : null;
    }

    public List<Ship> findByName(String name) {
        List<Ship> ships = new ArrayList<>();
        FindIterable<Document> documents = collection.find(
                Filters.regex("name", ".*" + name + ".*", "i"));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ships.add(documentToShip(doc));
            }
        }

        return ships;
    }

    public List<Ship> findByType(String type) {
        List<Ship> ships = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("type", type));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ships.add(documentToShip(doc));
            }
        }

        return ships;
    }

    public List<Ship> findByStatus(String status) {
        List<Ship> ships = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("status", status));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ships.add(documentToShip(doc));
            }
        }

        return ships;
    }

    public ObjectId save(Ship ship) {
        Document doc = shipToDocument(ship);

        if (ship.getId() == null) {
            collection.insertOne(doc);
            ship.setId((ObjectId) doc.get("_id"));
        } else {
            collection.replaceOne(Filters.eq("_id", ship.getId()), doc);
        }

        return ship.getId();
    }

    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Ship documentToShip(Document doc) {
        Ship ship = new Ship();
        ship.setId(doc.getObjectId("_id"));
        ship.setName(doc.getString("name"));
        ship.setType(doc.getString("type"));
        ship.setCapacity(doc.getInteger("capacity", 0));
        ship.setYearBuilt(doc.getInteger("yearBuilt", 0));
        ship.setRegistrationNumber(doc.getString("registrationNumber"));
        ship.setCurrentLocation(doc.getString("currentLocation"));
        ship.setStatus(doc.getString("status"));
        return ship;
    }

    private Document shipToDocument(Ship ship) {
        Document doc = new Document();

        if (ship.getId() != null) {
            doc.append("_id", ship.getId());
        }

        doc.append("name", ship.getName())
                .append("type", ship.getType())
                .append("capacity", ship.getCapacity())
                .append("registrationNumber", ship.getRegistrationNumber())
                .append("yearBuilt", ship.getYearBuilt())
                .append("currentLocation", ship.getCurrentLocation())
                .append("status", ship.getStatus());

        return doc;
    }
}
