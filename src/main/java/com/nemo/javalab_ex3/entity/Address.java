package com.nemo.javalab_ex3.entity;

public class Address {
    private int id;
    private String address;
    private String zipCode;

    public Address(int id, String address, String zipCode) {
        this.id = id;
        this.address = address;
        this.zipCode = zipCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
