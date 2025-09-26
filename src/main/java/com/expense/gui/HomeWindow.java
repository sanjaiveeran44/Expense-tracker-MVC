package com.expense.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeWindow {

    private JFrame frame;

    public HomeWindow() {
        frame = new JFrame("Expense Tracker");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.setLayout(new BorderLayout());

        JLabel heading = new JLabel("Expense Tracker", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(heading, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton expenseButton = new JButton("Expense Window");
        JButton categoryButton = new JButton("Category Window");

      
        buttonPanel.add(expenseButton);
        buttonPanel.add(categoryButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

      
        expenseButton.addActionListener(e->new ExpenseWindow());

        categoryButton.addActionListener(e->new CategoryWindow());

        frame.setVisible(true);
    }
}
