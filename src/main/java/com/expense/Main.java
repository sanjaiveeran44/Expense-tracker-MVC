package com.expense;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

import com.expense.gui.HomeWindow;
import com.expense.util.DataBaseConnection;

public class Main {
    public static void main(String[] args) {

        try(Connection conn = DataBaseConnection.getConnection()) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Connected to the database");
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            System.out.println("Couldn't set look and feel: " + e.getMessage());
        }
        catch(SQLException e) {
            System.out.println("Couldn't connect to the database: " + e.getMessage());
        }
        new HomeWindow(); 
    }
}
