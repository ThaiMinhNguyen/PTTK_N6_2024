package com.nemo.javalab_ex3.DAO;

import com.nemo.javalab_ex3.entity.Customer;
import com.nemo.javalab_ex3.entity.Address;

import java.sql.*;

public class CustomerDAO {

    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;

    public CustomerDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found: " + e.getMessage(), e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    public boolean addCustomer(Customer customer) throws SQLException {
        boolean isAdded = false;
        String addressSql = "INSERT INTO Address (address, zipCode) VALUES (?, ?)";
        String customerSql = "INSERT INTO Customer (name, phone, address_id) VALUES (?, ?, ?)";
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

                    PreparedStatement customerStatement = jdbcConnection.prepareStatement(customerSql);
                    customerStatement.setString(1, customer.getName());
                    customerStatement.setString(2, customer.getPhone());
                    customerStatement.setInt(3, addressId);

                    int customerRows = customerStatement.executeUpdate();

                    if (customerRows > 0) {
                        isAdded = true;
                    }

                    customerStatement.close();
                }

                addressKeys.close();
            }

            addressStatement.close();

            if (isAdded) {
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

        return isAdded;
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET name = ?, phone = ?, address_id = ? WHERE id = ?";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, customer.getName());
        statement.setString(2, customer.getPhone());
        statement.setInt(3, customer.getAddress().getId());
        statement.setInt(4, customer.getId());

        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    public Customer getCustomer(int customerId) throws SQLException {
        Customer customer = null;
        String sql = "SELECT c.id, c.name, c.phone, a.id AS address_id, a.address, a.zipCode "
                + "FROM Customer c "
                + "INNER JOIN Address a ON c.address_id = a.id WHERE c.id = ?";

        connect();

        try (PreparedStatement statement = jdbcConnection.prepareStatement(sql)) {
            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");
                    int addressId = resultSet.getInt("address_id");
                    String address = resultSet.getString("address");
                    String zipCode = resultSet.getString("zipCode");

                    Address customerAddress = new Address(addressId, address, zipCode);
                    customer = new Customer(customerId, name, phone, customerAddress);
                }
            }
        } finally {
            disconnect();  
        }

        return customer;
    }

}
