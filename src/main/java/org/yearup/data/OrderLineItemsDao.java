package org.yearup.data;

import java.math.BigDecimal;

public interface OrderLineItemsDao {
    void createOrderLine(int orderId, int productId, BigDecimal salesPrice, int quantity, BigDecimal discount);
}