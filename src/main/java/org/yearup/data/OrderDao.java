package org.yearup.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderDao {
    int checkout(int userId, LocalDateTime date, String address, String city, String state, String zip, BigDecimal shippingAmount);
}