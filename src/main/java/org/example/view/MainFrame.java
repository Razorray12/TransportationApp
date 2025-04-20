package org.example.view;

import org.example.util.MongoDBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private ShipPanel shipPanel;
    private CargoPanel cargoPanel;
    private RoutePanel routePanel;
    private CustomerPanel customerPanel;
    private EmployeePanel employeePanel;

    public MainFrame() {
        initComponents();
        setupLayout();
        setupListeners();

        setTitle("Морские грузоперевозки - Система управления");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        shipPanel = new ShipPanel();
        cargoPanel = new CargoPanel();
        routePanel = new RoutePanel();
        customerPanel = new CustomerPanel();
        employeePanel = new EmployeePanel();
    }

    private void setupLayout() {
        tabbedPane.addTab("Корабли", new ImageIcon(), shipPanel, "Управление кораблями");
        tabbedPane.addTab("Грузы", new ImageIcon(), cargoPanel, "Управление грузами");
        tabbedPane.addTab("Маршруты", new ImageIcon(), routePanel);
        tabbedPane.addTab("Клиенты", new ImageIcon(), customerPanel);
        tabbedPane.addTab("Сотрудники", new ImageIcon(), employeePanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MongoDBConnection.closeConnection();
                System.exit(0);
            }
        });

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    shipPanel.refreshData();
                    break;
                case 1:
                    cargoPanel.refreshData();
                    break;
                case 2:
                    routePanel.refreshData();
                    break;
                case 3:
                    customerPanel.refreshData();
                    break;
                case 4:
                    employeePanel.refreshData();
                    break;
            }
        });
    }
}
