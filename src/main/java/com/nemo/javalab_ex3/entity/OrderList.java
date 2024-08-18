package com.nemo.javalab_ex3.entity;

import java.util.List;

public class OrderList {
    private int id;
    private int bookId;
    private int orderId;

    public OrderList(int id, int bookId, int orderId) {
        this.id = id;
        this.bookId = bookId;
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}


