package org.example.view;

import org.example.contoller.CustomerController;
import org.example.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private final CustomerController customerController;

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchByComboBox;
    private JComboBox<String> sortByComboBox;
    private JRadioButton ascRadioButton;
    private JRadioButton descRadioButton;

    private JTextField nameField;
    private JTextField contactPersonField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField registrationNumberField;
    private JComboBox<String> customerTypeComboBox;
    private JTextArea notesArea;

    private Customer selectedCustomer;

    public CustomerPanel() {
        this.customerController = new CustomerController();
        initComponents();
        setupLayout();
        setupListeners();
        refreshData();
    }

    private void initComponents() {
        // Таблица
        String[] columnNames = {"ID", "Название", "Контактное лицо", "Email", "Телефон",
                "Адрес", "Рег. номер", "Тип клиента", "Примечания"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Поиск и сорт
        searchField = new JTextField(20);
        searchByComboBox = new JComboBox<>(new String[]{"Название", "Тип клиента"});
        sortByComboBox = new JComboBox<>(new String[]{"Название", "Тип клиента"});

        ascRadioButton = new JRadioButton("По возрастанию", true);
        descRadioButton = new JRadioButton("По убыванию");
        ButtonGroup sortGroup = new ButtonGroup();
        sortGroup.add(ascRadioButton);
        sortGroup.add(descRadioButton);

        // Поля для редактирования/добавления
        nameField = new JTextField(20);
        contactPersonField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(15);
        addressField = new JTextField(30);
        registrationNumberField = new JTextField(15);
        customerTypeComboBox = new JComboBox<>(new String[]{"individual", "company"});
        notesArea = new JTextArea(3, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
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
        JScrollPane tableScrollPane = new JScrollPane(customerTable);

        // Панель редактирования
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("Добавление/Редактирование клиента"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Первая колонка (метки)
        gbc.gridx = 0;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Название:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Контактное лицо:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Email:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Телефон:"), gbc);

        // Вторая колонка (поля)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(nameField, gbc);

        gbc.gridy = 1;
        editPanel.add(contactPersonField, gbc);

        gbc.gridy = 2;
        editPanel.add(emailField, gbc);

        gbc.gridy = 3;
        editPanel.add(phoneField, gbc);

        // Третья колонка (метки)
        gbc.gridx = 2;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Адрес:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Рег. номер:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Тип клиента:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Примечания:"), gbc);

        // Четвертая колонка (поля)
        gbc.gridx = 3;
        gbc.gridy = 0;
        editPanel.add(addressField, gbc);

        gbc.gridy = 1;
        editPanel.add(registrationNumberField, gbc);

        gbc.gridy = 2;
        editPanel.add(customerTypeComboBox, gbc);

        gbc.gridy = 3;
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        editPanel.add(notesScrollPane, gbc);

        // Кнопки действий
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton newButton = new JButton("Новый");
        JButton saveButton = new JButton("Сохранить");
        JButton deleteButton = new JButton("Удалить");

        newButton.addActionListener(e -> clearFields());
        saveButton.addActionListener(e -> saveCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());

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
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String id = tableModel.getValueAt(selectedRow, 0).toString();
                    selectedCustomer = customerController.getCustomerById(id);
                    if (selectedCustomer != null) {
                        populateFields(selectedCustomer);
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

        List<Customer> customers = customerController.getAllCustomersSorted(sortField, ascending);
        updateTable(customers);
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            refreshData();
            return;
        }

        List<Customer> customers = null;
        String searchBy = (String) searchByComboBox.getSelectedItem();

        if (searchBy.equals("Название")) {
            customers = customerController.searchCustomersByName(searchText);
        } else if (searchBy.equals("Тип клиента")) {
            customers = customerController.getCustomersByType(searchText);
        }

        if (customers != null) {
            updateTable(customers);
        }
    }

    private void updateTable(List<Customer> customers) {
        tableModel.setRowCount(0);

        for (Customer customer : customers) {
            tableModel.addRow(new Object[]{
                    customer.getId().toString(),
                    customer.getName(),
                    customer.getContactPerson(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getAddress(),
                    customer.getRegistrationNumber(),
                    customer.getCustomerType(),
                    customer.getNotes()
            });
        }
    }

    private void populateFields(Customer customer) {
        nameField.setText(customer.getName());
        contactPersonField.setText(customer.getContactPerson());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone());
        addressField.setText(customer.getAddress());
        registrationNumberField.setText(customer.getRegistrationNumber());
        customerTypeComboBox.setSelectedItem(customer.getCustomerType());
        notesArea.setText(customer.getNotes());
    }

    private void clearFields() {
        selectedCustomer = null;
        nameField.setText("");
        contactPersonField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        registrationNumberField.setText("");
        customerTypeComboBox.setSelectedIndex(0);
        notesArea.setText("");
        customerTable.clearSelection();
    }

    private void saveCustomer() {
        try {
            String name = nameField.getText().trim();
            String contactPerson = contactPersonField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String registrationNumber = registrationNumberField.getText().trim();
            String customerType = (String) customerTypeComboBox.getSelectedItem();
            String notes = notesArea.getText().trim();

            if (name.isEmpty() || contactPerson.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Обязательные поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedCustomer == null) {
                // Создание нового
                selectedCustomer = new Customer(name, contactPerson, email, phone, address,
                        registrationNumber, customerType, notes);
            } else {
                // Обновление существующего
                selectedCustomer.setName(name);
                selectedCustomer.setContactPerson(contactPerson);
                selectedCustomer.setEmail(email);
                selectedCustomer.setPhone(phone);
                selectedCustomer.setAddress(address);
                selectedCustomer.setRegistrationNumber(registrationNumber);
                selectedCustomer.setCustomerType(customerType);
                selectedCustomer.setNotes(notes);
            }

            boolean success = customerController.saveCustomer(selectedCustomer);
            if (success) {
                JOptionPane.showMessageDialog(this, "Клиент успешно сохранен", "Информация", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении клиента", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Произошла ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        if (selectedCustomer != null) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите удалить клиента " + selectedCustomer.getName() + "?",
                    "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                boolean success = customerController.deleteCustomer(selectedCustomer.getId().toString());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Клиент успешно удален", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при удалении клиента", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите клиента для удаления!", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getSortField() {
        String sortBy = (String) sortByComboBox.getSelectedItem();

        return switch (sortBy) {
            case "Название" -> "name";
            case "Тип клиента" -> "customerType";
            default -> "name";
        };
    }
}