package org.example.view;

import org.example.contoller.ShipController;
import org.example.model.Ship;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShipPanel extends JPanel {
    private final ShipController shipController;

    private JTable shipTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchByComboBox;
    private JComboBox<String> sortByComboBox;
    private JRadioButton ascRadioButton;
    private JRadioButton descRadioButton;

    private JTextField nameField;
    private JTextField typeField;
    private JTextField capacityField;
    private JTextField registrationField;
    private JTextField yearBuiltField;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;

    private Ship selectedShip;

    public ShipPanel() {
        this.shipController = new ShipController();
        initComponents();
        setupLayout();
        setupListeners();
        refreshData();
    }

    private void initComponents() {
        // Таблица
        String[] columnNames = {"ID", "Название", "Тип", "Вместимость", "Рег. номер", "Год постройки", "Текущее местоположение", "Статус"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        shipTable = new JTable(tableModel);
        shipTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchField = new JTextField(20);
        searchByComboBox = new JComboBox<>(new String[]{"Название", "Тип", "Статус"});
        sortByComboBox = new JComboBox<>(new String[]{"Название", "Тип", "Вместимость", "Год постройки"});

        ascRadioButton = new JRadioButton("По возрастанию", true);
        descRadioButton = new JRadioButton("По убыванию");
        ButtonGroup sortGroup = new ButtonGroup();
        sortGroup.add(ascRadioButton);
        sortGroup.add(descRadioButton);

        // Поля для редактирования/добавления
        nameField = new JTextField(20);
        typeField = new JTextField(20);
        capacityField = new JTextField(10);
        registrationField = new JTextField(15);
        yearBuiltField = new JTextField(10);
        locationField = new JTextField(20);
        statusComboBox = new JComboBox<>(new String[]{"active", "maintenance", "inactive"});
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
        JScrollPane tableScrollPane = new JScrollPane(shipTable);

        // Панель редактирования
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("Добавление/Редактирование корабля"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Первая колонка (метки)
        gbc.gridx = 0;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Название:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Тип:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Вместимость:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Рег. номер:"), gbc);

        // Вторая колонка (поля)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(nameField, gbc);

        gbc.gridy = 1;
        editPanel.add(typeField, gbc);

        gbc.gridy = 2;
        editPanel.add(capacityField, gbc);

        gbc.gridy = 3;
        editPanel.add(registrationField, gbc);

        // Третья колонка (метки)
        gbc.gridx = 2;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Год постройки:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Местоположение:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Статус:"), gbc);

        // Четвертая колонка (поля)
        gbc.gridx = 3;
        gbc.gridy = 0;
        editPanel.add(yearBuiltField, gbc);

        gbc.gridy = 1;
        editPanel.add(locationField, gbc);

        gbc.gridy = 2;
        editPanel.add(statusComboBox, gbc);

        // Кнопки действий
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton newButton = new JButton("Новый");
        JButton saveButton = new JButton("Сохранить");
        JButton deleteButton = new JButton("Удалить");

        newButton.addActionListener(e -> clearFields());
        saveButton.addActionListener(e -> saveShip());
        deleteButton.addActionListener(e -> deleteShip());

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
        shipTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = shipTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String id = tableModel.getValueAt(selectedRow, 0).toString();
                    selectedShip = shipController.getShipById(id);
                    if (selectedShip != null) {
                        populateFields(selectedShip);
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

        List<Ship> ships = shipController.getAllShipsSorted(sortField, ascending);
        updateTable(ships);
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            refreshData();
            return;
        }

        List<Ship> ships = null;
        String searchBy = (String) searchByComboBox.getSelectedItem();

        if (searchBy.equals("Название")) {
            ships = shipController.searchShipsByName(searchText);
        } else if (searchBy.equals("Тип")) {
            ships = shipController.getShipsByType(searchText);
        } else if (searchBy.equals("Статус")) {
            ships = shipController.getShipsByStatus(searchText);
        }

        if (ships != null) {
            updateTable(ships);
        }
    }

    private void updateTable(List<Ship> ships) {
        tableModel.setRowCount(0);

        for (Ship ship : ships) {
            tableModel.addRow(new Object[]{
                    ship.getId().toString(),
                    ship.getName(),
                    ship.getType(),
                    ship.getCapacity(),
                    ship.getRegistrationNumber(),
                    ship.getYearBuilt(),
                    ship.getCurrentLocation(),
                    ship.getStatus()
            });
        }
    }

    private void populateFields(Ship ship) {
        nameField.setText(ship.getName());
        typeField.setText(ship.getType());
        capacityField.setText(String.valueOf(ship.getCapacity()));
        registrationField.setText(ship.getRegistrationNumber());
        yearBuiltField.setText(String.valueOf(ship.getYearBuilt()));
        locationField.setText(ship.getCurrentLocation());
        statusComboBox.setSelectedItem(ship.getStatus());
    }

    private void clearFields() {
        selectedShip = null;
        nameField.setText("");
        typeField.setText("");
        capacityField.setText("");
        registrationField.setText("");
        yearBuiltField.setText("");
        locationField.setText("");
        statusComboBox.setSelectedIndex(0);
        shipTable.clearSelection();
    }

    private void saveShip() {
        try {
            String name = nameField.getText().trim();
            String type = typeField.getText().trim();
            int capacity = Integer.parseInt(capacityField.getText().trim());
            String regNumber = registrationField.getText().trim();
            int yearBuilt = Integer.parseInt(yearBuiltField.getText().trim());
            String location = locationField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();

            if (name.isEmpty() || type.isEmpty() || regNumber.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedShip == null) {
                // Создание нового
                selectedShip = new Ship(name, type, capacity, regNumber, yearBuilt, location, status);
            } else {
                // Обновление существующего
                selectedShip.setName(name);
                selectedShip.setType(type);
                selectedShip.setCapacity(capacity);
                selectedShip.setRegistrationNumber(regNumber);
                selectedShip.setYearBuilt(yearBuilt);
                selectedShip.setCurrentLocation(location);
                selectedShip.setStatus(status);
            }

            boolean success = shipController.saveShip(selectedShip);
            if (success) {
                JOptionPane.showMessageDialog(this, "Корабль успешно сохранен", "Информация", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении корабля", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Вместимость и год постройки должны быть числами!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteShip() {
        if (selectedShip != null) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите удалить корабль " + selectedShip.getName() + "?",
                    "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                boolean success = shipController.deleteShip(selectedShip.getId().toString());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Корабль успешно удален", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при удалении корабля", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите корабль для удаления!", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getSortField() {
        String sortBy = (String) sortByComboBox.getSelectedItem();

        return switch (sortBy) {
            case "Название" -> "name";
            case "Тип" -> "type";
            case "Вместимость" -> "capacity";
            case "Год постройки" -> "yearBuilt";
            default -> "name";
        };
    }
}

