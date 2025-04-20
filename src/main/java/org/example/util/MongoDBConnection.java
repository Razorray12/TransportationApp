package org.example.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

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
            boolean cargoExists = collectionExists("cargos");
            boolean routesExists = collectionExists("routes");
            boolean customersExists = collectionExists("customers");
            boolean employeesExists = collectionExists("employees");

            // Коллекция ships
            if (!shipsExists) {
                database.createCollection("ships");
                System.out.println("Created 'ships' collection");

                List<Document> ships = List.of(
                        new Document()
                                .append("name", "Морской волк")
                                .append("capacity", 5000)
                                .append("type", "Грузовой")
                                .append("status", "Активен"),

                        new Document()
                                .append("name", "Океанский гигант")
                                .append("capacity", 15000)
                                .append("type", "Контейнеровоз")
                                .append("status", "Активен"),

                        new Document()
                                .append("name", "Быстрый клипер")
                                .append("capacity", 3000)
                                .append("type", "Ролкер")
                                .append("status", "На ремонте"),

                        new Document()
                                .append("name", "Северное сияние")
                                .append("capacity", 8000)
                                .append("type", "Танкер")
                                .append("status", "Активен"),

                        new Document()
                                .append("name", "Южный крест")
                                .append("capacity", 6000)
                                .append("type", "Рефрижератор")
                                .append("status", "В резерве")
                );

                database.getCollection("ships").insertMany(ships);
                System.out.println("Inserted 5 test ships");
            }

            // Коллекция cargo
            if (!cargoExists) {
                database.createCollection("cargos");
                System.out.println("Created 'cargos' collection");

                List<Document> cargoList = List.of(
                        new Document()
                                .append("description", "Стальные трубы")
                                .append("weight", 2500.0)
                                .append("type", "Металлопрокат")
                                .append("shipId", new ObjectId())  // Генерируем новый ObjectId для связи с кораблем
                                .append("customerId", new ObjectId()) // Генерируем новый ObjectId для связи с клиентом
                                .append("originPort", "Москва")
                                .append("destinationPort", "Нью-Йорк")
                                .append("loadingDate", new Date())
                                .append("deliveryDate", new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // +7 дней
                                .append("status", "В ожидании"),

                        new Document()
                                .append("description", "Электроника")
                                .append("weight", 1800.0)
                                .append("type", "Бытовая техника")
                                .append("shipId", new ObjectId())
                                .append("customerId", new ObjectId())
                                .append("originPort", "Санкт-Петербург")
                                .append("destinationPort", "Роттердам")
                                .append("loadingDate", new Date())
                                .append("deliveryDate", new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000)) // +5 дней
                                .append("status", "В пути"),

                        new Document()
                                .append("description", "Нефть сырая")
                                .append("weight", 12000.0)
                                .append("type", "Нефтепродукты")
                                .append("shipId", new ObjectId())
                                .append("customerId", new ObjectId())
                                .append("originPort", "Новороссийск")
                                .append("destinationPort", "Шанхай")
                                .append("loadingDate", new Date())
                                .append("deliveryDate", new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000)) // +14 дней
                                .append("status", "Загружено"),

                        new Document()
                                .append("description", "Зерно пшеничное")
                                .append("weight", 8000.0)
                                .append("type", "Сельхозпродукция")
                                .append("shipId", new ObjectId())
                                .append("customerId", new ObjectId())
                                .append("originPort", "Ростов-на-Дону")
                                .append("destinationPort", "Александрия")
                                .append("loadingDate", new Date())
                                .append("deliveryDate", new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000)) // +10 дней
                                .append("status", "В ожидании"),

                        new Document()
                                .append("description", "Автомобили")
                                .append("weight", 4500.0)
                                .append("type", "Транспорт")
                                .append("shipId", new ObjectId())
                                .append("customerId", new ObjectId())
                                .append("originPort", "Казань")
                                .append("destinationPort", "Йокогама")
                                .append("loadingDate", new Date())
                                .append("deliveryDate", new Date(System.currentTimeMillis() + 21 * 24 * 60 * 60 * 1000)) // +21 день
                                .append("status", "Доставлено")
                );

                database.getCollection("cargos").insertMany(cargoList);
                System.out.println("Inserted 5 test cargo items");
            }

            // Коллекция routes
            if (!routesExists) {
                database.createCollection("routes");
                System.out.println("Created 'routes' collection");

                List<Document> routes = List.of(
                        new Document()
                                .append("name", "Трансатлантический")
                                .append("startPort", "Роттердам")
                                .append("endPort", "Нью-Йорк")
                                .append("distance", 5800)
                                .append("estimatedDays", 14)
                                .append("description", "Основной трансатлантический маршрут")
                                .append("status", "active"),

                        new Document()
                                .append("name", "Азиатский экспресс")
                                .append("startPort", "Шанхай")
                                .append("endPort", "Лос-Анджелес")
                                .append("distance", 11000)
                                .append("estimatedDays", 21)
                                .append("description", "Маршрут между Азией и Северной Америкой")
                                .append("status", "active"),

                        new Document()
                                .append("name", "Северный морской путь")
                                .append("startPort", "Мурманск")
                                .append("endPort", "Владивосток")
                                .append("distance", 14200)
                                .append("estimatedDays", 28)
                                .append("description", "Арктический маршрут")
                                .append("status", "inactive"),

                        new Document()
                                .append("name", "Средиземноморский")
                                .append("startPort", "Генуя")
                                .append("endPort", "Стамбул")
                                .append("distance", 2500)
                                .append("estimatedDays", 7)
                                .append("description", "Маршрут по Средиземному морю")
                                .append("status", "active"),

                        new Document()
                                .append("name", "Африканский круиз")
                                .append("startPort", "Кейптаун")
                                .append("endPort", "Лагос")
                                .append("distance", 4200)
                                .append("estimatedDays", 12)
                                .append("description", "Маршрут вдоль западного побережья Африки")
                                .append("status", "active")
                );

                database.getCollection("routes").insertMany(routes);
                System.out.println("Inserted 5 test routes");
            }

            // Коллекция customers
            if (!customersExists) {
                database.createCollection("customers");
                System.out.println("Created 'customers' collection");

                List<Document> customers = List.of(
                        new Document()
                                .append("name", "ООО 'Глобал Трейд'")
                                .append("contactPerson", "Иванов Сергей Петрович")
                                .append("email", "info@globaltrade.ru")
                                .append("phone", "+74951234567")
                                .append("address", "Москва, ул. Торговая, 15")
                                .append("contractNumber", "GT-2023-001"),

                        new Document()
                                .append("name", "АО 'Морские перевозки'")
                                .append("contactPerson", "Петрова Мария Ивановна")
                                .append("email", "office@seacargo.com")
                                .append("phone", "+78122456789")
                                .append("address", "Санкт-Петербург, Невский пр., 100")
                                .append("contractNumber", "MP-2023-045"),

                        new Document()
                                .append("name", "ИП Сидоров А.В.")
                                .append("contactPerson", "Сидоров Алексей Владимирович")
                                .append("email", "sidorov.av@mail.ru")
                                .append("phone", "+79031234567")
                                .append("address", "Новороссийск, ул. Портовиков, 5")
                                .append("contractNumber", "SA-2023-012"),

                        new Document()
                                .append("name", "ООО 'Восточный экспресс'")
                                .append("contactPerson", "Ким Лидия Дмитриевна")
                                .append("email", "vostok@express.ru")
                                .append("phone", "+74232223344")
                                .append("address", "Владивосток, ул. Набережная, 10")
                                .append("contractNumber", "VE-2023-078"),

                        new Document()
                                .append("name", "ЗАО 'Северные грузы'")
                                .append("contactPerson", "Смирнов Дмитрий Олегович")
                                .append("email", "north@cargo.ru")
                                .append("phone", "+78152567890")
                                .append("address", "Архангельск, пр. Ломоносова, 25")
                                .append("contractNumber", "SG-2023-033")
                );

                database.getCollection("customers").insertMany(customers);
                System.out.println("Inserted 5 test customers");
            }

            // Коллекция employees
            if (!employeesExists) {
                database.createCollection("employees");
                System.out.println("Created 'employees' collection");

                List<Document> employees = List.of(
                        new Document()
                                .append("firstName", "Иван")
                                .append("lastName", "Петров")
                                .append("position", "Капитан")
                                .append("hireDate", new java.util.Date())
                                .append("email", "ivan.petrov@example.com")
                                .append("phone", "+79161234567")
                                .append("address", "ул. Морская, 10, Санкт-Петербург")
                                .append("passportNumber", "1234567890")
                                .append("status", "active"),

                        new Document()
                                .append("firstName", "Алексей")
                                .append("lastName", "Сидоров")
                                .append("position", "Первый помощник")
                                .append("hireDate", new java.util.Date())
                                .append("email", "alex.sidorov@example.com")
                                .append("phone", "+79162345678")
                                .append("address", "ул. Портовый проспект, 15, Владивосток")
                                .append("passportNumber", "2345678901")
                                .append("status", "active"),

                        new Document()
                                .append("firstName", "Мария")
                                .append("lastName", "Иванова")
                                .append("position", "Штурман")
                                .append("hireDate", new java.util.Date())
                                .append("email", "maria.ivanova@example.com")
                                .append("phone", "+79163456789")
                                .append("address", "пр. Набережный, 20, Калининград")
                                .append("passportNumber", "3456789012")
                                .append("status", "active"),

                        new Document()
                                .append("firstName", "Дмитрий")
                                .append("lastName", "Кузнецов")
                                .append("position", "Боцман")
                                .append("hireDate", new java.util.Date())
                                .append("email", "dmitry.kuznetsov@example.com")
                                .append("phone", "+79164567890")
                                .append("address", "ул. Рыбацкая, 5, Архангельск")
                                .append("passportNumber", "4567890123")
                                .append("status", "on leave"),

                        new Document()
                                .append("firstName", "Ольга")
                                .append("lastName", "Смирнова")
                                .append("position", "Кок")
                                .append("hireDate", new java.util.Date())
                                .append("email", "olga.smirnova@example.com")
                                .append("phone", "+79165678901")
                                .append("address", "пер. Кухонный, 3, Новороссийск")
                                .append("passportNumber", "5678901234")
                                .append("status", "active")
                );

                database.getCollection("employees").insertMany(employees);
                System.out.println("Inserted 5 test employees");
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