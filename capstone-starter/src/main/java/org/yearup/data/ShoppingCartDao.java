package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);

    List<ShoppingCartItem> getItems (int userId);

    ShoppingCart addProductToCart(int userId, int productId);

    void updateCartItem(int userId, int productId, int quantity);

    void clearCart(int userId);
}
