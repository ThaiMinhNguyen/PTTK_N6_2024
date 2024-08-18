package com.nemo.javalab_ex3.DAO;

import com.nemo.javalab_ex3.entity.Book;
import com.nemo.javalab_ex3.entity.Order;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class OrderDAO {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;

    public OrderDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
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

    public boolean createOrder(Order order, List<Book> books) throws SQLException {
        boolean isCreated = false;
        String orderSql = "INSERT INTO Orders (orderDate, customer_id) VALUES (?, ?)";
        String orderListSql = "INSERT INTO OrderList (order_id, book_id) VALUES (?, ?)";

        connect();

        try {
            jdbcConnection.setAutoCommit(false);

            PreparedStatement orderStatement = jdbcConnection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStatement.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            orderStatement.setInt(2, order.getCustomer().getId());

            int affectedRows = orderStatement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet orderKeys = orderStatement.getGeneratedKeys();
                if (orderKeys.next()) {
                    int orderId = orderKeys.getInt(1);

                    for (Book book : books) {
                        PreparedStatement orderListStatement = jdbcConnection.prepareStatement(orderListSql);
                        orderListStatement.setInt(1, orderId);
                        orderListStatement.setInt(2, book.getId());
                        orderListStatement.executeUpdate();
                        orderListStatement.close();
                    }

                    isCreated = true;
                }
                orderKeys.close();
            }

            orderStatement.close();

            if (isCreated) {
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
        return isCreated;
    }

}
