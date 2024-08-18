package com.nemo.javalab_ex3.DAO;

import com.nemo.javalab_ex3.entity.Account;
import com.nemo.javalab_ex3.entity.Customer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public boolean registerAccount(Account account, Customer customer) throws SQLException {
        boolean isRegistered = false;
        String addressSql = "INSERT INTO Address (address, zipCode) VALUES (?, ?)";
        String customerSql = "INSERT INTO Customer (name, phone, address_id) VALUES (?, ?, ?)";
        String accountSql = "INSERT INTO Account (username, password, customer_id) VALUES (?, ?, ?)";

        connect();

        try {
            jdbcConnection.setAutoCommit(false);

            PreparedStatement addressStatement = jdbcConnection.prepareStatement(addressSql, Statement.RETURN_GENERATED_KEYS);
            addressStatement.setString(1, customer.getAddress().getAddress());
            addressStatement.setString(2, customer.getAddress().getZipCode());
            int affectedRows = addressStatement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet addressKeys = addressStatement.getGeneratedKeys();
                if (addressKeys.next()) {
                    int addressId = addressKeys.getInt(1);

                    PreparedStatement customerStatement = jdbcConnection.prepareStatement(customerSql, Statement.RETURN_GENERATED_KEYS);
                    customerStatement.setString(1, customer.getName());
                    customerStatement.setString(2, customer.getPhone());
                    customerStatement.setInt(3, addressId);
                    affectedRows = customerStatement.executeUpdate();

                    if (affectedRows > 0) {
                        ResultSet customerKeys = customerStatement.getGeneratedKeys();
                        if (customerKeys.next()) {
                            int customerId = customerKeys.getInt(1);

                            PreparedStatement accountStatement = jdbcConnection.prepareStatement(accountSql);
                            accountStatement.setString(1, account.getUsername());
                            accountStatement.setString(2, account.getPassword());
                            accountStatement.setInt(3, customerId);

                            int accountRows = accountStatement.executeUpdate();

                            if (accountRows > 0) {
                                isRegistered = true;
                            }

                            accountStatement.close();
                        }

                        customerKeys.close();
                    }

                    customerStatement.close();
                }

                addressKeys.close();
            }

            addressStatement.close();

            if (isRegistered) {
                jdbcConnection.commit();
            } else {
                jdbcConnection.rollback();
            }
        } catch (SQLException e) {
            jdbcConnection.rollback();
            throw e;
        } finally {
            jdbcConnection.setAutoCommit(true);
            disconnect();
        }

        return isRegistered;
    }
    
    
    public Account loginAccount(String username, String password) throws SQLException {
        Account account = null;
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            account = new Account();
            account.setUsername(resultSet.getString("username"));
            account.setPassword(resultSet.getString("password"));
            account.setCustomer(new CustomerDAO(jdbcURL, jdbcUsername, jdbcPassword).getCustomer(Integer.parseInt(resultSet.getString("customer_id"))));
        }
        resultSet.close();
        statement.close();
        disconnect();
        return account;
    }
}
