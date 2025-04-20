package org.example.view;

import org.example.contoller.EmployeeController;
import org.example.model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeePanel extends JPanel {
    private final EmployeeController employeeController;

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchByComboBox;
    private JComboBox<String> sortByComboBox;
    private JRadioButton ascRadioButton;
    private JRadioButton descRadioButton;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField positionField;
    private JTextField hireDateField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField passportField;
    private JComboBox<String> statusComboBox;

    private Employee selectedEmployee;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EmployeePanel() {
        this.employeeController = new EmployeeController();
        initComponents();
        setupLayout();
        setupListeners();
        refreshData();
    }

    private void initComponents() {
        // Таблица
        String[] columnNames = {"ID", "Имя", "Фамилия", "Должность", "Дата найма",
                "Email", "Телефон", "Адрес", "Номер паспорта", "Статус"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Поиск и сорт
        searchField = new JTextField(20);
        searchByComboBox = new JComboBox<>(new String[]{"Имя/Фамилия", "Должность", "Статус"});
        sortByComboBox = new JComboBox<>(new String[]{"Имя", "Фамилия", "Должность", "Дата найма"});

        ascRadioButton = new JRadioButton("По возрастанию", true);
        descRadioButton = new JRadioButton("По убыванию");
        ButtonGroup sortGroup = new ButtonGroup();
        sortGroup.add(ascRadioButton);
        sortGroup.add(descRadioButton);

        // Поля для редактирования/добавления
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        positionField = new JTextField(20);
        hireDateField = new JTextField(10);
        emailField = new JTextField(20);
        phoneField = new JTextField(15);
        addressField = new JTextField(30);
        passportField = new JTextField(15);
        statusComboBox = new JComboBox<>(new String[]{"active", "on leave", "terminated"});
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
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        // Панель редактирования
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("Добавление/Редактирование сотрудника"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Первая колонка (метки)
        gbc.gridx = 0;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Имя:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Фамилия:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Должность:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Дата найма (ГГГГ-ММ-ДД):"), gbc);

        // Вторая колонка (поля)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.add(firstNameField, gbc);

        gbc.gridy = 1;
        editPanel.add(lastNameField, gbc);

        gbc.gridy = 2;
        editPanel.add(positionField, gbc);

        gbc.gridy = 3;
        editPanel.add(hireDateField, gbc);

        // Третья колонка (метки)
        gbc.gridx = 2;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Email:"), gbc);

        gbc.gridy = 1;
        editPanel.add(new JLabel("Телефон:"), gbc);

        gbc.gridy = 2;
        editPanel.add(new JLabel("Адрес:"), gbc);

        gbc.gridy = 3;
        editPanel.add(new JLabel("Статус:"), gbc);

        // Четвертая колонка (поля)
        gbc.gridx = 3;
        gbc.gridy = 0;
        editPanel.add(emailField, gbc);

        gbc.gridy = 1;
        editPanel.add(phoneField, gbc);

        gbc.gridy = 2;
        editPanel.add(addressField, gbc);

        gbc.gridy = 3;
        editPanel.add(statusComboBox, gbc);

        // Пятая колонка (метки)
        gbc.gridx = 4;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Номер паспорта:"), gbc);

        // Шестая колонка (поля)
        gbc.gridx = 5;
        gbc.gridy = 0;
        editPanel.add(passportField, gbc);

        // Кнопки действий
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton newButton = new JButton("Новый");
        JButton saveButton = new JButton("Сохранить");
        JButton deleteButton = new JButton("Удалить");

        newButton.addActionListener(e -> clearFields());
        saveButton.addActionListener(e -> saveEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());

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
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String id = tableModel.getValueAt(selectedRow, 0).toString();
                    selectedEmployee = employeeController.getEmployeeById(id);
                    if (selectedEmployee != null) {
                        populateFields(selectedEmployee);
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

        List<Employee> employees = employeeController.getAllEmployeesSorted(sortField, ascending);
        updateTable(employees);
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            refreshData();
            return;
        }

        List<Employee> employees = null;
        String searchBy = (String) searchByComboBox.getSelectedItem();

        if (searchBy.equals("Имя/Фамилия")) {
            employees = employeeController.searchEmployeesByName(searchText);
        } else if (searchBy.equals("Должность")) {
            employees = employeeController.getEmployeesByPosition(searchText);
        } else if (searchBy.equals("Статус")) {
            employees = employeeController.getEmployeesByStatus(searchText);
        }

        if (employees != null) {
            updateTable(employees);
        }
    }

    private void updateTable(List<Employee> employees) {
        tableModel.setRowCount(0);

        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                    employee.getId().toString(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getPosition(),
                    formatDate(employee.getHireDate()),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.getAddress(),
                    employee.getPassportNumber(),
                    employee.getStatus()
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

    private void populateFields(Employee employee) {
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        positionField.setText(employee.getPosition());
        hireDateField.setText(formatDate(employee.getHireDate()));
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhone());
        addressField.setText(employee.getAddress());
        passportField.setText(employee.getPassportNumber());
        statusComboBox.setSelectedItem(employee.getStatus());
    }

    private void clearFields() {
        selectedEmployee = null;
        firstNameField.setText("");
        lastNameField.setText("");
        positionField.setText("");
        hireDateField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        passportField.setText("");
        statusComboBox.setSelectedIndex(0);
        employeeTable.clearSelection();
    }

    private void saveEmployee() {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String position = positionField.getText().trim();
            Date hireDate = parseDate(hireDateField.getText());
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String passport = passportField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();

            if (firstName.isEmpty() || lastName.isEmpty() || position.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Обязательные поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedEmployee == null) {
                // Создание нового
                selectedEmployee = new Employee(firstName, lastName, position, hireDate, email,
                        phone, address, passport, status);
            } else {
                // Обновление существующего
                selectedEmployee.setFirstName(firstName);
                selectedEmployee.setLastName(lastName);
                selectedEmployee.setPosition(position);
                selectedEmployee.setHireDate(hireDate);
                selectedEmployee.setEmail(email);
                selectedEmployee.setPhone(phone);
                selectedEmployee.setAddress(address);
                selectedEmployee.setPassportNumber(passport);
                selectedEmployee.setStatus(status);
            }

            boolean success = employeeController.saveEmployee(selectedEmployee);
            if (success) {
                JOptionPane.showMessageDialog(this, "Сотрудник успешно сохранен", "Информация", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении сотрудника", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Неверный формат даты. Используйте ГГГГ-ММ-ДД", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        if (selectedEmployee != null) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите удалить сотрудника " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + "?",
                    "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                boolean success = employeeController.deleteEmployee(selectedEmployee.getId().toString());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Сотрудник успешно удален", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при удалении сотрудника", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите сотрудника для удаления!", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getSortField() {
        String sortBy = (String) sortByComboBox.getSelectedItem();

        return switch (sortBy) {
            case "Имя" -> "firstName";
            case "Фамилия" -> "lastName";
            case "Должность" -> "position";
            case "Дата найма" -> "hireDate";
            default -> "lastName";
        };
    }
}