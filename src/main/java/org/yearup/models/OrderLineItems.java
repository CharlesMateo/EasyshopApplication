package org.yearup.models;

import java.math.BigDecimal;

public class OrderLineItems {
    private int orderId;
    private BigDecimal sales_price;
    private int quantity;
    private BigDecimal discount;

    public OrderLineItems( int orderId, BigDecimal sales_price, int quantity, BigDecimal discount) {
        this.orderId = orderId;
        this.sales_price = sales_price;
        this.quantity = quantity;
        this.discount = discount;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getSales_price() {
        return sales_price;
    }

    public void setSales_price(BigDecimal sales_price) {
        this.sales_price = sales_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}