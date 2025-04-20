package org.example.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static final String DATABASE_NAME = "shipping_db";

    private static final String CONNECTION_STRING =
            "mongodb://host.docker.internal:27017,host.docker.internal:27018,host.docker.internal:27019/?replicaSet=rs0";

    private MongoDBConnection() {
        // Private constructor to prevent instantiation
    }

    public static synchronized MongoDatabase getDatabase() {
        if (database == null) {
            try {
                System.out.println("Connecting to MongoDB using: " + CONNECTION_STRING);

                ConnectionString connString = new ConnectionString(CONNECTION_STRING);

                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connString)
                        .build();

                mongoClient = MongoClients.create(settings);
                database = mongoClient.getDatabase(DATABASE_NAME);

                database.runCommand(new Document("ping", 1));
                System.out.println("Successfully connected to MongoDB database: " + DATABASE_NAME);

                createCollectionsIfNotExist();
            } catch (Exception e) {
                System.err.println("Failed to connect to MongoDB: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("MongoDB connection failed", e);
            }
        }
        return database;
    }

    private static void createCollectionsIfNotExist() {
        try {
            boolean shipsExists = collectionExists("ships");
            boolean cargoExists = collectionExists("cargo");
            boolean routesExists = collectionExists("routes");
            boolean customersExists = collectionExists("customers");
            boolean employeesExists = collectionExists("employees");

            if (!shipsExists) {
                database.createCollection("ships");
                System.out.println("Created 'ships' collection");
                // Insert test data
                database.getCollection("ships").insertOne(new Document()
                        .append("name", "Морской волк")
                        .append("capacity", 5000)
                        .append("type", "Грузовой")
                        .append("status", "Активен"));
                System.out.println("Inserted test data into 'ships' collection");
            }
            if (!cargoExists) {
                database.createCollection("cargo");
                System.out.println("Created 'cargo' collection");
            }
            if (!routesExists) {
                database.createCollection("routes");
                System.out.println("Created 'routes' collection");
            }
            if (!customersExists) {
                database.createCollection("customers");
                System.out.println("Created 'customers' collection");
            }
            if (!employeesExists) {
                database.createCollection("employees");
                System.out.println("Created 'employees' collection");
            }
        } catch (Exception e) {
            System.err.println("Error creating collections: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean collectionExists(String collectionName) {
        try {
            for (String name : database.listCollectionNames()) {
                if (name.equals(collectionName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error checking if collection exists: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            try {
                mongoClient.close();
                System.out.println("MongoDB connection closed");
            } catch (Exception e) {
                System.err.println("Error closing MongoDB connection: " + e.getMessage());
                e.printStackTrace();
            } finally {
                mongoClient = null;
                database = null;
            }
        }
    }
}