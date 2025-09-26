package com.expense.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


import com.expense.model.Category;
import com.expense.util.DataBaseConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.expense.model.Expense;



public class MainDAO {

    private static final String INSERT_EXPENSE = "INSERT INTO expenses (name, amount, date, cid, categoryName) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM categories";
    private static final String SELECT_ALL_EXPENSES = "SELECT * FROM expenses";
    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id = ?";
    private static final String DELETE_EXPENSE = "DELETE FROM expenses WHERE id = ?";
    private static final String CATEGORY_INC_COUNT = "UPDATE categories SET count = count + 1 WHERE id = ?";
    private static final String UPDATE_EXPENSE = "UPDATE expenses SET description = ?, amount = ?, date = ?, id = ? WHERE id = ?";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String INSERT_CATEGORY = "INSERT INTO categories (name) VALUES (?)";

    public int addCategory(Category category) throws SQLException {
        try (
            Connection conn = DataBaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS);
        ) {
            ps.setString(1, category.getName());
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }

            try ( ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        try(
            Connection conn = DataBaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_CATEGORIES);
            ResultSet rs = ps.executeQuery();
        ){
            List<Category> categories = new ArrayList<>();
            while(rs.next()){
                Category category = new Category(rs.getInt("id"), rs.getString("name"), rs.getInt("count"));
                categories.add(category);
            }
            return categories;
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {
        try(
            Connection conn = DataBaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_EXPENSES);
            ResultSet rs = ps.executeQuery();
        ){
            List<Expense> expenses = new ArrayList<>();
            while(rs.next()){
                Expense expense = new Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("amount"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getInt("cid"),
                    rs.getString("categoryName")
                );
                expenses.add(expense);
            }
            return expenses;
        }
    }

    public void deleteCategory(int cid) throws SQLException {
        try(
            Connection conn = DataBaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_CATEGORY);
            ){
                ps.setInt(1, cid);
                int rowsAffected = ps.executeUpdate();
                if(rowsAffected == 0){
                    throw new SQLException("Deleting category failed, no rows affected.");
                }
            }
    }

    public void updateCategory(int cid, String name) throws SQLException{
        try(Connection conn = DataBaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY);
        ){
            ps.setString(1,name);
            ps.setInt(2,cid);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Updating category failed, no rows affected.");
            }
        }
    }

    public void addExpense(Expense expense) throws SQLException {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_EXPENSE);
             PreparedStatement ps2 = conn.prepareStatement(CATEGORY_INC_COUNT,Statement.RETURN_GENERATED_KEYS);
        ) {
            if (expense.getCid() == -1) {
                Category category = new Category(0,expense.getCategoryName(),0);
                int cid = addCategory(category);
                expense.setCid(cid);
            }

            ps.setString(1, expense.getName());
            ps.setDouble(2, expense.getAmount());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(expense.getDate()));
            ps.setInt(4, expense.getCid());
            ps.setString(5, expense.getCategoryName());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }
            
            ps2.setInt(1, expense.getCid());
            ps2.executeUpdate();
        }
    }
}