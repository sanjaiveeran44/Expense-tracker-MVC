package com.expense.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.expense.dao.MainDAO;
import com.expense.model.Category;
import java.sql.SQLException;
import java.util.List;


public class CategoryWindow {
    private JFrame frame;
    private JTextField nameField;
    private JTextField updateTextField;
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private MainDAO dao = new MainDAO();
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;


    public CategoryWindow() {
        frame = new JFrame("Category Window");
        InitializeComponents();
        setUpLayout();
        setUpActionListeners();
        loadCategories();
        frame.setVisible(true);
    }

    private void InitializeComponents(){
        String[] columnNames = {"Category ID", "Category Name", "No of Expenses"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 50, 400, 200);
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        addButton = new JButton("Add");
        nameField = new JTextField(20);
        updateTextField = new JTextField(20);
    }

    private void setUpLayout(){
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        JPanel inputpanel = new JPanel();
        inputpanel.add(new JLabel("Category Name:"));
        inputpanel.add(nameField);
        inputpanel.add(addButton);
        JPanel OptionPanel = new JPanel(new FlowLayout());
        OptionPanel.add(deleteButton);
        OptionPanel.add(updateButton);
        OptionPanel.add(updateTextField);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputpanel, BorderLayout.SOUTH);
        frame.add(OptionPanel, BorderLayout.EAST);
    }

    private void setUpActionListeners(){
        addButton.addActionListener(e->{
            addCategory();
        });

        deleteButton.addActionListener(e->{
            deleteCategory();
        });

        updateButton.addActionListener(e->{
            updateCategory();
        });

    }

    private void addCategory(){
        String name = nameField.getText();
        Category category = new Category(0,name,0);

        if(name.isEmpty()){
            JOptionPane.showMessageDialog(frame, "Category name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            nameField.setText("");
            return;
        }

        try {
            dao.addCategory(category);
            loadCategories();
            nameField.setText("");
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    private void loadCategories(){
        try{
        tableModel.setRowCount(0);
        List<Category> categories = dao.getAllCategories();
        for(Category category : categories){
            tableModel.addRow(new Object[]{category.getCid(), category.getName(), category.getCount()});
        }
    }
        catch(SQLException e){
            System.out.println("Error loading categories: " + e.getMessage());
        }
    }

    private void deleteCategory(){
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(frame, "Please select a category to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cid = (int) tableModel.getValueAt(selectedRow,0);
        try{
            dao.deleteCategory(cid);
            loadCategories();
        }
        catch(SQLException e){
            System.out.println("Error in deleting category:" + e.getMessage());
        }
    }

    private void updateCategory(){
        int selectedRow = table.getSelectedRow();
        String name = updateTextField.getText();
        if(selectedRow == -1 || updateTextField.getText().isEmpty()){
            JOptionPane.showMessageDialog(frame, "Please select a category to delete and Enter the name to update", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int cid = (int) tableModel.getValueAt(selectedRow,0);
        try{
            dao.updateCategory(cid, name);
            loadCategories();
            updateTextField.setText("");
        }
        catch(SQLException e){
            System.out.println("Error in updating category" + e.getMessage());
        }
    }
}
