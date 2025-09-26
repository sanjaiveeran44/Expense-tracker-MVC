package com.expense.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import com.expense.dao.MainDAO;
import com.expense.model.Expense;
import java.util.List;
import java.sql.*;
import com.expense.util.DataBaseConnection;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



public class ExpenseWindow {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private MainDAO dao = new MainDAO();
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTextField nameField;
    private JTextField updateTextField;
    private JTextField amountField;
    private JTextField dateField;
    private JScrollPane scrollPane;
    private JTextField categoryField;

    public ExpenseWindow() {
        frame = new JFrame("Expense Window");
        InitializeComponents();
        setUpLayout();
        setUpExpenseActionListeners();
        loadExpenses();
        frame.setVisible(true);
    }
    private void InitializeComponents(){
        String[] columnNames = {"Expense Name","Amount","Date","Category ID","Category Name"};
        tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50,50,400,200);
        deleteButton = new JButton("Delete");
        updateButton = new JButton("update");
        addButton = new JButton("Add");
        nameField = new JTextField(20);
        updateTextField = new JTextField(20);
        amountField = new JTextField(20);
        dateField = new JTextField(20);
        categoryField = new JTextField(20);

    }

    private void setUpLayout(){
        frame.setLayout(new BorderLayout());
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel heading = new JLabel("Expense Window",SwingConstants.CENTER);
        heading.setFont(new Font("Arial",Font.BOLD,28));
        heading.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Expense Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Category name:"));
        inputPanel.add(categoryField);

        JPanel optionPanel = new JPanel(new FlowLayout());
        optionPanel.add(addButton);
        optionPanel.add(deleteButton);
        optionPanel.add(updateButton);
        optionPanel.add(updateTextField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,10,5,10);
        topPanel.add(heading,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,10,10,10);
        topPanel.add(optionPanel,gbc);

        frame.add(scrollPane,BorderLayout.CENTER);
        frame.add(inputPanel,BorderLayout.SOUTH);
        frame.add(topPanel,BorderLayout.NORTH);


    }
    private void loadExpenses(){
        try{
        tableModel.setRowCount(0);
        List<Expense> expenses = dao.getAllExpenses();
        for(Expense expense : expenses){
            tableModel.addRow(new Object[]{expense.getName(), expense.getAmount(), expense.getDate(), expense.getCid(), expense.getCategoryName()});
        }
    }
        catch(SQLException e){
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }
    private void setUpExpenseActionListeners(){
        addButton.addActionListener(e->{
            addExpense();
        });

        deleteButton.addActionListener(e->{
            deleteExpense();
        });

        updateButton.addActionListener(e->{
            updateExpense();
        });
    }
    private int getCid(String categoryName) {
        try (Connection conn = new DataBaseConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM categories WHERE name = ?");
        ) {
            ps.setString(1, categoryName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void addExpense(){
        try {
            String expenseName = nameField.getText();
            String amountText = amountField.getText();
            String date = dateField.getText();
            String categoryName = categoryField.getText().trim();
            
            if (expenseName.trim().isEmpty() || amountText.trim().isEmpty() || date.trim().isEmpty() || categoryName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Amount must be greater than zero.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int cid = getCid(categoryName);
            LocalDateTime expenseDate;
            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
                expenseDate = LocalDateTime.parse(date + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(frame, 
                    "Invalid date format. Please use YYYY-MM-DD format.", 
                    "Date Format Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            Expense expense = new Expense(0,expenseName, amount, expenseDate, cid, categoryName);
            dao.addExpense(expense);
            loadExpenses();
            nameField.setText("");
            amountField.setText("");
            dateField.setText("");
            categoryField.setText("");            
        }
        catch(SQLException e){
            System.out.println("Error in adding expense:" + e.getMessage());
        }
    }
    private void deleteExpense(){
        
    }
    private void updateExpense(){
        
    }
}
