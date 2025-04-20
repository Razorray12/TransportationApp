package org.example.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import org.example.model.Cargo;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.util.MongoDBConnection;

import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    private final MongoCollection<Document> collection;

    public CargoDAO() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("cargos");
    }

    public List<Cargo> findAll() {
        List<Cargo> cargos = new ArrayList<>();
        FindIterable<Document> documents = collection.find();

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                cargos.add(documentToCargo(doc));
            }
        }

        return cargos;
    }

    public List<Cargo> findAll(String sortField, boolean ascending) {
        List<Cargo> cargos = new ArrayList<>();
        FindIterable<Document> documents;

        if (ascending) {
            documents = collection.find().sort(Sorts.ascending(sortField));
        } else {
            documents = collection.find().sort(Sorts.descending(sortField));
        }

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                cargos.add(documentToCargo(doc));
            }
        }

        return cargos;
    }

    public Cargo findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToCargo(doc) : null;
    }

    public List<Cargo> findByDescription(String description) {
        List<Cargo> cargos = new ArrayList<>();
        FindIterable<Document> documents = collection.find(
                Filters.regex("description", ".*" + description + ".*", "i"));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                cargos.add(documentToCargo(doc));
            }
        }

        return cargos;
    }

    public List<Cargo> findByShipId(ObjectId shipId) {
        List<Cargo> cargos = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("shipId", shipId));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                cargos.add(documentToCargo(doc));
            }
        }

        return cargos;
    }

    public List<Cargo> findByCustomerId(ObjectId customerId) {
        List<Cargo> cargos = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("customerId", customerId));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                cargos.add(documentToCargo(doc));
            }
        }

        return cargos;
    }

    public List<Cargo> findByStatus(String status) {
        List<Cargo> cargos = new ArrayList<>();
        FindIterable<Document> documents = collection.find(Filters.eq("status", status));

        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                cargos.add(documentToCargo(doc));
            }
        }

        return cargos;
    }

    public ObjectId save(Cargo cargo) {
        Document doc = cargoToDocument(cargo);

        if (cargo.getId() == null) {
            collection.insertOne(doc);
            cargo.setId((ObjectId) doc.get("_id"));
        } else {
            collection.replaceOne(Filters.eq("_id", cargo.getId()), doc);
        }

        return cargo.getId();
    }

    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
    private Cargo documentToCargo(Document doc) {
        Cargo cargo = new Cargo();
        cargo.setId(doc.getObjectId("_id"));
        cargo.setDescription(doc.getString("description"));
        cargo.setWeight(doc.getDouble("weight"));
        cargo.setType(doc.getString("type"));
        cargo.setShipId(doc.getObjectId("shipId"));
        cargo.setCustomerId(doc.getObjectId("customerId"));
        cargo.setOriginPort(doc.getString("originPort"));
        cargo.setDestinationPort(doc.getString("destinationPort"));
        cargo.setLoadingDate(doc.getDate("loadingDate"));
        cargo.setDeliveryDate(doc.getDate("deliveryDate"));
        cargo.setStatus(doc.getString("status"));

        return cargo;
    }

    private Document cargoToDocument(Cargo cargo) {
        Document doc = new Document();

        if (cargo.getId() != null) {
            doc.append("_id", cargo.getId());
        }

        doc.append("description", cargo.getDescription())
                .append("weight", cargo.getWeight())
                .append("type", cargo.getType())
                .append("shipId", cargo.getShipId())
                .append("customerId", cargo.getCustomerId())
                .append("originPort", cargo.getOriginPort())
                .append("destinationPort", cargo.getDestinationPort())
                .append("loadingDate", cargo.getLoadingDate())
                .append("deliveryDate", cargo.getDeliveryDate())
                .append("status", cargo.getStatus());

        return doc;
    }
}
