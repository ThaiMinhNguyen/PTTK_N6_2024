/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nemo.javalab_ex1.DAO;

import com.nemo.javalab_ex1.entity.Account;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class AccountDAO {

    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;

    public AccountDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found: " + e.getMessage(), e);
            }
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL, jdbcUsername, jdbcPassword);
        }
    }
    
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }
     
     public boolean registerAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (username, password) VALUES (?, ?)";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, account.getUsername());
        statement.setString(2, account.getPassword());

        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    public Account loginAccount(String username, String password) throws SQLException {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            account = new Account();
            account.setUsername(resultSet.getString("username"));
            account.setPassword(resultSet.getString("password"));
        }
        resultSet.close();
        statement.close();
        disconnect();
        return account;
    }

    public boolean accountExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM accounts WHERE username = ?";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        boolean exists = resultSet.getInt(1) > 0;

        resultSet.close();
        statement.close();
        disconnect();
        return exists;
    }
}
