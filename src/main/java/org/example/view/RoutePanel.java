package org.example.view;

import org.example.contoller.EmployeeController;
import org.example.contoller.RouteController;
import org.example.model.Employee;
import org.example.model.Route;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoutePanel extends JPanel {
    private final RouteController routeController;
    private final EmployeeController employeeController;

    private JTable routeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchByComboBox;
    private JComboBox<String> sortByComboBox;
    private JRadioButton ascRadioButton;
    private JRadioButton descRadioButton;

    private JTextField nameField;
    private JTextField startPortField;
    private JTextField endPortField;
    private JTextField distanceField;
    private JTextField estimatedDaysField;
    private JComboBox<Employee> employeeComboBox;
    private JTextField descriptionField;
    private JComboBox<String> statusComboBox;

    private Route selectedRoute;

    public RoutePanel() {
        this.routeController = new RouteController();
        this.employeeController = new EmployeeController();
        initComponents();
        setupLayout();
        setupListeners();
        refreshData();
    }

    private void initComponents() {
        // Таблица
        String[] columnNames = {"ID", "Название", "Порт отправления", "Порт назначения", "Расстояние", "Оценка дней", "Сотрудник", "Описание", "Статус"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        routeTable = new JTable(tableModel);
        routeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchField = new JTextField(20);
        searchByComboBox = new JComboBox<>(new String[]{"Название", "Статус"});
        sortByComboBox = new JComboBox<>(new String[]{"Название", "Расстояние", "Оценка дней"});

        ascRadioButton = new JRadioButton("По возрастанию", true);
        descRadioButton = new JRadioButton("По убыванию");
        ButtonGroup sortGroup = new ButtonGroup();
        sortGroup.add(ascRadioButton);
        sortGroup.add(descRadioButton);

        // Поля для редактирования/добавления
        nameField = new JTextField(20);
        startPortField = new JTextField(20);
        endPortField = new JTextField(20);
        distanceField = new JTextField(10);
        estimatedDaysField = new JTextField(10);

        List<Employee> employees = employeeController.getAllEmployees();
        employeeComboBox = new JComboBox<>(employees.toArray(new Employee[0]));

        descriptionField = new JTextField(20);
        statusComboBox = new JComboBox<>(new String[]{"active", "inactive"});
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Панель поиска и сортировки
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Поиск:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("По:"));
        searchPanel.add(searchByComboBox);
        searchPanel.add(new JLabel("Сортировать по:"));
        searchPanel.add(sortByComboBox);
        searchPanel.add(ascRadioButton);
        searchPanel.add(descRadioButton);

        JButton searchButton = new JButton("Искать");
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);

        JButton resetButton = new JButton("Сбросить");
        resetButton.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });
        searchPanel.add(resetButton);

        // Панель с таблицей
        JScrollPane tableScrollPane = new JScrollPane(routeTable);

        // Панель редактирования
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("Добавление/Редактирование маршрута"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Первая колонка (метки)
        gbc.gridx = 0;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Название:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Порт отправления:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Порт назначения:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Расстояние (км):"), gbc);

        // Вторая колонка (поля)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(nameField, gbc);

        gbc.gridy = 1;
        editPanel.add(startPortField, gbc);

        gbc.gridy = 2;
        editPanel.add(endPortField, gbc);

        gbc.gridy = 3;
        editPanel.add(distanceField, gbc);

        // Третья колонка (метки)
        gbc.gridx = 2;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Оценка дней:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Сотрудник:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Описание:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Статус:"), gbc);

        // Четвертая колонка (поля)
        gbc.gridx = 3;
        gbc.gridy = 0;
        editPanel.add(estimatedDaysField, gbc);

        gbc.gridy = 1;
        editPanel.add(employeeComboBox, gbc);

        gbc.gridy = 2;
        editPanel.add(descriptionField, gbc);

        gbc.gridy = 3;
        editPanel.add(statusComboBox, gbc);

        // Кнопки действий
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton newButton = new JButton("Новый");
        JButton saveButton = new JButton("Сохранить");
        JButton deleteButton = new JButton("Удалить");

        newButton.addActionListener(e -> clearFields());
        saveButton.addActionListener(e -> saveRoute());
        deleteButton.addActionListener(e -> deleteRoute());

        buttonPanel.add(newButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        editPanel.add(buttonPanel, gbc);

        // Добавление всех панелей в основную
        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(editPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        routeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = routeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String id = tableModel.getValueAt(selectedRow, 0).toString();
                    selectedRoute = routeController.getRouteById(id);
                    if (selectedRoute != null) {
                        populateFields(selectedRoute);
                    }
                }
            }
        });

        sortByComboBox.addActionListener(e -> refreshData());
        ascRadioButton.addActionListener(e -> refreshData());
        descRadioButton.addActionListener(e -> refreshData());
    }

    public void refreshData() {
        String sortField = getSortField();
        boolean ascending = ascRadioButton.isSelected();

        List<Route> routes = routeController.getAllRoutesSorted(sortField, ascending);
        updateTable(routes);
        updateEmployeeComboBox();
    }

    private void updateEmployeeComboBox() {
        Employee selectedEmployee = (Employee) employeeComboBox.getSelectedItem();

        employeeComboBox.removeAllItems();
        List<Employee> employees = employeeController.getAllEmployees();
        for (Employee employee : employees) {
            employeeComboBox.addItem(employee);
        }

        if (selectedEmployee != null) {
            for (int i = 0; i < employeeComboBox.getItemCount(); i++) {
                Employee employee = employeeComboBox.getItemAt(i);
                if (employee.getId().equals(selectedEmployee.getId())) {
                    employeeComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            refreshData();
            return;
        }

        List<Route> routes = null;
        String searchBy = (String) searchByComboBox.getSelectedItem();

        if (searchBy.equals("Название")) {
            routes = routeController.searchRoutesByName(searchText);
        } else if (searchBy.equals("Статус")) {
            routes = routeController.getRoutesByStatus(searchText);
        }

        if (routes != null) {
            updateTable(routes);
        }
    }

    private void updateTable(List<Route> routes) {
        tableModel.setRowCount(0);

        for (Route route : routes) {
            Employee employee = employeeController.getEmployeeById(route.getAssignedEmployeeId() != null ? route.getAssignedEmployeeId().toString() : null);
            tableModel.addRow(new Object[]{
                    route.getId().toString(),
                    route.getName(),
                    route.getStartPort(),
                    route.getEndPort(),
                    route.getDistance(),
                    route.getEstimatedDays(),
                    employee != null ? employee.getFirstName() + " " + employee.getLastName() : "N/A",
                    route.getDescription(),
                    route.getStatus()
            });
        }
    }

    private void populateFields(Route route) {
        nameField.setText(route.getName());
        startPortField.setText(route.getStartPort());
        endPortField.setText(route.getEndPort());
        distanceField.setText(String.valueOf(route.getDistance()));
        estimatedDaysField.setText(String.valueOf(route.getEstimatedDays()));
        descriptionField.setText(route.getDescription());
        statusComboBox.setSelectedItem(route.getStatus());

        // Установка сотрудника
        if (route.getAssignedEmployeeId() != null) {
            for (int i = 0; i < employeeComboBox.getItemCount(); i++) {
                Employee employee = employeeComboBox.getItemAt(i);
                if (employee.getId().equals(route.getAssignedEmployeeId())) {
                    employeeComboBox.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            employeeComboBox.setSelectedIndex(-1);
        }
    }

    private void clearFields() {
        selectedRoute = null;
        nameField.setText("");
        startPortField.setText("");
        endPortField.setText("");
        distanceField.setText("");
        estimatedDaysField.setText("");
        employeeComboBox.setSelectedIndex(-1);
        descriptionField.setText("");
        statusComboBox.setSelectedIndex(0);
        routeTable.clearSelection();
    }

    private void saveRoute() {
        try {
            String name = nameField.getText().trim();
            String startPort = startPortField.getText().trim();
            String endPort = endPortField.getText().trim();
            String distanceStr = distanceField.getText().trim();
            String estimatedDaysStr = estimatedDaysField.getText().trim();
            Employee employee = (Employee) employeeComboBox.getSelectedItem();
            String description = descriptionField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();

            if (name.isEmpty() || startPort.isEmpty() || endPort.isEmpty() || distanceStr.isEmpty() || estimatedDaysStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Все обязательные поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double distance = Double.parseDouble(distanceStr);
            int estimatedDays = Integer.parseInt(estimatedDaysStr);

            if (selectedRoute == null) {
                selectedRoute = new Route(name, startPort, endPort, distance, estimatedDays,
                        employee != null ? employee.getId() : null, description, status);
            } else {
                selectedRoute.setName(name);
                selectedRoute.setStartPort(startPort);
                selectedRoute.setEndPort(endPort);
                selectedRoute.setDistance(distance);
                selectedRoute.setEstimatedDays(estimatedDays);
                selectedRoute.setAssignedEmployeeId(employee != null ? employee.getId() : null);
                selectedRoute.setDescription(description);
                selectedRoute.setStatus(status);
            }

            boolean success = routeController.saveRoute(selectedRoute);
            if (success) {
                JOptionPane.showMessageDialog(this, "Маршрут успешно сохранен", "Информация", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении маршрута", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Расстояние и оценка дней должны быть числами!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода данных!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoute() {
        if (selectedRoute != null) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите удалить маршрут " + selectedRoute.getName() + "?",
                    "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                boolean success = routeController.deleteRoute(selectedRoute.getId().toString());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Маршрут успешно удален", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при удалении маршрута", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите маршрут для удаления!", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getSortField() {
        String sortBy = (String) sortByComboBox.getSelectedItem();

        return switch (sortBy) {
            case "Название" -> "name";
            case "Расстояние" -> "distance";
            case "Оценка дней" -> "estimatedDays";
            default -> "name";
        };
    }
}