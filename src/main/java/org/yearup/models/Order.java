package org.yearup.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private int user_id;
    private LocalDateTime date;
    private String address;
    private String city;
    private String state;
    private String zip;
    private BigDecimal sipping_amount;

    public Order(int user_id, LocalDateTime date, String address, String city, String state, String zip, BigDecimal sipping_amount) {
        this.user_id = user_id;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.sipping_amount = sipping_amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public BigDecimal getSipping_amount() {
        return sipping_amount;
    }

    public void setSipping_amount(BigDecimal sipping_amount) {
        this.sipping_amount = sipping_amount;
    }
}