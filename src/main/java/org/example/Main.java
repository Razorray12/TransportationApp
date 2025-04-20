package org.example;

import org.example.util.MongoDBConnection;
import org.example.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        try {
            // Упрощенное сообщение для пользователя
            System.out.println("Initializing MongoDB connection...");

            // Проверка подключения
            MongoDBConnection.getDatabase();
            System.out.println("✓ Successfully connected to MongoDB cluster");

            // Запуск GUI в правильном потоке
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    System.out.println("Launching application UI...");
                    new MainFrame().setVisible(true);  // Явное отображение окна
                    System.out.println("Application started successfully");
                } catch (Exception e) {
                    handleFatalError("UI initialization failed", e);
                }
            });

        } catch (Exception e) {
            handleFatalError("MongoDB connection failed", e);
        }
    }

    private static void handleFatalError(String message, Exception e) {
        System.err.println("⛔ " + message + ": " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}