package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class ShoppingCartItem {
    private Product product;
    private int quantity;
    private BigDecimal discountPercent;

    public ShoppingCartItem() {
        this.quantity = 1;
        this.discountPercent = BigDecimal.ZERO;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(quantity, 0); // Ensure non-negative quantity
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent != null ? discountPercent : BigDecimal.ZERO;
    }

    @JsonIgnore
    public int getProductId() {
        return product != null ? product.getProductId() : -1; // Return -1 if product is null
    }

    public BigDecimal getLineTotal() {
        if (product == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal basePrice = product.getPrice();
        BigDecimal subtotal = basePrice.multiply(new BigDecimal(quantity));
        BigDecimal discountAmount = subtotal.multiply(discountPercent);

        return subtotal.subtract(discountAmount);
    }
}
