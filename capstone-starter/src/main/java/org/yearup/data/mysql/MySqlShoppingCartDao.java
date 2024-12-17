package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();
        String sql = "SELECT product_id, quantity FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                ShoppingCartItem item = new ShoppingCartItem();
                item.setQuantity(quantity);
                // Fetch product details and set to item (Product fetching logic can be added here)
                item.setProduct(null); // Placeholder for Product fetching logic
                cart.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching shopping cart for user ID: " + userId, e);
        }

        return cart;
    }

    @Override
    public List<ShoppingCartItem> getItems(int userId) {
        List<ShoppingCartItem> items = new ArrayList<>();
        String sql = "SELECT product_id, quantity FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                ShoppingCartItem item = new ShoppingCartItem();
                item.setQuantity(quantity);
                // Fetch product details and set to item (Product fetching logic can be added here)
                item.setProduct(null); // Placeholder for Product fetching logic
                items.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching items for user ID: " + userId, e);
        }

        return items;
    }

    @Override
    public ShoppingCart addProductToCart(int userId, int productId) {
        String sql = "MERGE INTO shopping_cart AS target " +
                "USING (SELECT ? AS user_id, ? AS product_id) AS source " +
                "ON target.user_id = source.user_id AND target.product_id = source.product_id " +
                "WHEN MATCHED THEN " +
                "   UPDATE SET target.quantity = target.quantity + 1 " +
                "WHEN NOT MATCHED THEN " +
                "   INSERT (user_id, product_id, quantity) VALUES (source.user_id, source.product_id, 1);";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding product to cart", e);
        }

        return getByUserId(userId);
    }

    @Override
    public void updateCartItem(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cart item for user ID: " + userId + " and product ID: " + productId, e);
        }
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart for user ID: " + userId, e);
        }
    }
}

