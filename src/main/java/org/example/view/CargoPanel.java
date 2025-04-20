package org.example.view;

import org.example.contoller.CargoController;
import org.example.contoller.CustomerController;
import org.example.contoller.ShipController;
import org.example.model.Cargo;
import org.example.model.Customer;
import org.example.model.Ship;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CargoPanel extends JPanel {
    private final CargoController cargoController;
    private final ShipController shipController;
    private final CustomerController customerController;

    private JTable cargoTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchByComboBox;
    private JComboBox<String> sortByComboBox;
    private JRadioButton ascRadioButton;
    private JRadioButton descRadioButton;

    private JTextField descriptionField;
    private JTextField weightField;
    private JTextField typeField;
    private JComboBox<Ship> shipComboBox;
    private JComboBox<Customer> customerComboBox;
    private JTextField originPortField;
    private JTextField destPortField;
    private JTextField loadingDateField;
    private JTextField deliveryDateField;
    private JComboBox<String> statusComboBox;

    private Cargo selectedCargo;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CargoPanel() {
        this.cargoController = new CargoController();
        this.shipController = new ShipController();
        this.customerController = new CustomerController();
        initComponents();
        setupLayout();
        setupListeners();
        refreshData();
    }

    private void initComponents() {
        // Таблица
        String[] columnNames = {"ID", "Описание", "Вес", "Тип", "Корабль", "Клиент",
                "Порт отправления", "Порт назначения", "Дата загрузки", "Дата доставки", "Статус"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cargoTable = new JTable(tableModel);
        cargoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Поиск и сорт
        searchField = new JTextField(20);
        searchByComboBox = new JComboBox<>(new String[]{"Описание", "Тип", "Статус"});
        sortByComboBox = new JComboBox<>(new String[]{"Описание", "Вес", "Дата загрузки", "Дата доставки"});

        ascRadioButton = new JRadioButton("По возрастанию", true);
        descRadioButton = new JRadioButton("По убыванию");
        ButtonGroup sortGroup = new ButtonGroup();
        sortGroup.add(ascRadioButton);
        sortGroup.add(descRadioButton);

        // Поля для редактирования/добавления
        descriptionField = new JTextField(20);
        weightField = new JTextField(10);
        typeField = new JTextField(15);

        List<Ship> ships = shipController.getAllShips();
        shipComboBox = new JComboBox<>(ships.toArray(new Ship[0]));

        List<Customer> customers = customerController.getAllCustomers();
        customerComboBox = new JComboBox<>(customers.toArray(new Customer[0]));

        originPortField = new JTextField(15);
        destPortField = new JTextField(15);
        loadingDateField = new JTextField(10);
        deliveryDateField = new JTextField(10);
        statusComboBox = new JComboBox<>(new String[]{"loading", "in transit", "delivered"});
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
        JScrollPane tableScrollPane = new JScrollPane(cargoTable);

        // Панель редактирования
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("Добавление/Редактирование груза"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Первая колонка (метки)
        gbc.gridx = 0;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Описание:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Вес (т):"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Тип:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Корабль:"), gbc);

        // Вторая колонка (поля)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(descriptionField, gbc);

        gbc.gridy = 1;
        editPanel.add(weightField, gbc);

        gbc.gridy = 2;
        editPanel.add(typeField, gbc);

        gbc.gridy = 3;
        editPanel.add(shipComboBox, gbc);

        // Третья колонка (метки)
        gbc.gridx = 2;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Клиент:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Порт отправления:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Порт назначения:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Статус:"), gbc);

        // Четвертая колонка (поля)
        gbc.gridx = 3;
        gbc.gridy = 0;
        editPanel.add(customerComboBox, gbc);

        gbc.gridy = 1;
        editPanel.add(originPortField, gbc);

        gbc.gridy = 2;
        editPanel.add(destPortField, gbc);

        gbc.gridy = 3;
        editPanel.add(statusComboBox, gbc);

        // Пятая колонка (метки)
        gbc.gridx = 4;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Дата загрузки (ГГГГ-ММ-ДД):"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Дата доставки (ГГГГ-ММ-ДД):"), gbc);

        // Шестая колонка (поля)
        gbc.gridx = 5;
        gbc.gridy = 0;
        editPanel.add(loadingDateField, gbc);

        gbc.gridy = 1;
        editPanel.add(deliveryDateField, gbc);

        // Кнопки действий
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton newButton = new JButton("Новый");
        JButton saveButton = new JButton("Сохранить");
        JButton deleteButton = new JButton("Удалить");

        newButton.addActionListener(e -> clearFields());
        saveButton.addActionListener(e -> saveCargo());
        deleteButton.addActionListener(e -> deleteCargo());

        buttonPanel.add(newButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        editPanel.add(buttonPanel, gbc);

        // Добавление всех панелей в основную
        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(editPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        cargoTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = cargoTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String id = tableModel.getValueAt(selectedRow, 0).toString();
                    selectedCargo = cargoController.getCargoById(id);
                    if (selectedCargo != null) {
                        populateFields(selectedCargo);
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

        List<Cargo> cargos = cargoController.getAllCargosSorted(sortField, ascending);
        updateTable(cargos);

        // Обновляем списки кораблей и клиентов
        updateShipComboBox();
        updateCustomerComboBox();
    }

    private void updateShipComboBox() {
        Ship selectedShip = (Ship) shipComboBox.getSelectedItem();

        shipComboBox.removeAllItems();
        List<Ship> ships = shipController.getAllShips();
        for (Ship ship : ships) {
            shipComboBox.addItem(ship);
        }

        if (selectedShip != null) {
            for (int i = 0; i < shipComboBox.getItemCount(); i++) {
                Ship ship = shipComboBox.getItemAt(i);
                if (ship.getId().equals(selectedShip.getId())) {
                    shipComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void updateCustomerComboBox() {
        Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();

        customerComboBox.removeAllItems();
        List<Customer> customers = customerController.getAllCustomers();
        for (Customer customer : customers) {
            customerComboBox.addItem(customer);
        }

        if (selectedCustomer != null) {
            for (int i = 0; i < customerComboBox.getItemCount(); i++) {
                Customer customer = customerComboBox.getItemAt(i);
                if (customer.getId().equals(selectedCustomer.getId())) {
                    customerComboBox.setSelectedIndex(i);
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

        List<Cargo> cargos = null;
        String searchBy = (String) searchByComboBox.getSelectedItem();

        if (searchBy.equals("Описание")) {
            cargos = cargoController.searchCargosByDescription(searchText);
        } else if (searchBy.equals("Статус")) {
            cargos = cargoController.getCargosByStatus(searchText);
        }

        if (cargos != null) {
            updateTable(cargos);
        }
    }

    private void updateTable(List<Cargo> cargos) {
        tableModel.setRowCount(0);

        for (Cargo cargo : cargos) {
            Ship ship = shipController.getShipById(cargo.getShipId().toString());
            Customer customer = customerController.getCustomerById(cargo.getCustomerId().toString());

            tableModel.addRow(new Object[]{
                    cargo.getId().toString(),
                    cargo.getDescription(),
                    cargo.getWeight(),
                    cargo.getType(),
                    ship != null ? ship.getName() : "N/A",
                    customer != null ? customer.getName() : "N/A",
                    cargo.getOriginPort(),
                    cargo.getDestinationPort(),
                    formatDate(cargo.getLoadingDate()),
                    formatDate(cargo.getDeliveryDate()),
                    cargo.getStatus()
            });
        }
    }

    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "";
    }

    private Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return dateFormat.parse(dateStr.trim());
    }

    private void populateFields(Cargo cargo) {
        descriptionField.setText(cargo.getDescription());
        weightField.setText(String.valueOf(cargo.getWeight()));
        typeField.setText(cargo.getType());

        // Установка корабля
        for (int i = 0; i < shipComboBox.getItemCount(); i++) {
            Ship ship = shipComboBox.getItemAt(i);
            if (ship.getId().equals(cargo.getShipId())) {
                shipComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Установка клиента
        for (int i = 0; i < customerComboBox.getItemCount(); i++) {
            Customer customer = customerComboBox.getItemAt(i);
            if (customer.getId().equals(cargo.getCustomerId())) {
                customerComboBox.setSelectedIndex(i);
                break;
            }
        }

        originPortField.setText(cargo.getOriginPort());
        destPortField.setText(cargo.getDestinationPort());
        loadingDateField.setText(formatDate(cargo.getLoadingDate()));
        deliveryDateField.setText(formatDate(cargo.getDeliveryDate()));
        statusComboBox.setSelectedItem(cargo.getStatus());
    }

    private void clearFields() {
        selectedCargo = null;
        descriptionField.setText("");
        weightField.setText("");
        typeField.setText("");
        if (shipComboBox.getItemCount() > 0) shipComboBox.setSelectedIndex(0);
        if (customerComboBox.getItemCount() > 0) customerComboBox.setSelectedIndex(0);
        originPortField.setText("");
        destPortField.setText("");
        loadingDateField.setText("");
        deliveryDateField.setText("");
        statusComboBox.setSelectedIndex(0);
        cargoTable.clearSelection();
    }

    private void saveCargo() {
        try {
            String description = descriptionField.getText().trim();
            double weight = Double.parseDouble(weightField.getText().trim());
            String type = typeField.getText().trim();
            Ship ship = (Ship) shipComboBox.getSelectedItem();
            Customer customer = (Customer) customerComboBox.getSelectedItem();
            String originPort = originPortField.getText().trim();
            String destPort = destPortField.getText().trim();
            Date loadingDate = parseDate(loadingDateField.getText());
            Date deliveryDate = parseDate(deliveryDateField.getText());
            String status = (String) statusComboBox.getSelectedItem();

            if (description.isEmpty() || type.isEmpty() || originPort.isEmpty() || destPort.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (ship == null || customer == null) {
                JOptionPane.showMessageDialog(this, "Необходимо выбрать корабль и клиента!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedCargo == null) {
                // Создание нового
                selectedCargo = new Cargo(description, weight, type, ship.getId(), customer.getId(),
                        originPort, destPort, loadingDate, deliveryDate, status);
            } else {
                // Обновление существующего
                selectedCargo.setDescription(description);
                selectedCargo.setWeight(weight);
                selectedCargo.setType(type);
                selectedCargo.setShipId(ship.getId());
                selectedCargo.setCustomerId(customer.getId());
                selectedCargo.setOriginPort(originPort);
                selectedCargo.setDestinationPort(destPort);
                selectedCargo.setLoadingDate(loadingDate);
                selectedCargo.setDeliveryDate(deliveryDate);
                selectedCargo.setStatus(status);
            }

            boolean success = cargoController.saveCargo(selectedCargo);
            if (success) {
                JOptionPane.showMessageDialog(this, "Груз успешно сохранен", "Информация", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении груза", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Вес должен быть числом!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Неверный формат даты. Используйте ГГГГ-ММ-ДД", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCargo() {
        if (selectedCargo != null) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите удалить груз " + selectedCargo.getDescription() + "?",
                    "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                boolean success = cargoController.deleteCargo(selectedCargo.getId().toString());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Груз успешно удален", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при удалении груза", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите груз для удаления!", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getSortField() {
        String sortBy = (String) sortByComboBox.getSelectedItem();

        return switch (sortBy) {
            case "Описание" -> "description";
            case "Вес" -> "weight";
            case "Дата загрузки" -> "loadingDate";
            case "Дата доставки" -> "deliveryDate";
            default -> "description";
        };
    }
}
