package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.yearup.data.mysql.MySqlProductDao.mapRow;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String sql = "SELECT * FROM shopping_cart" +
                " JOIN products ON  shopping_cart.product_id = products.product_id" +
                "  WHERE user_id = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet row = statement.executeQuery();

            while (row.next())
            {
                Product product = mapRow(row);
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                shoppingCartItem.setProduct(product);
                shoppingCartItem.setQuantity(row.getInt("quantity"));
                shoppingCart.add(shoppingCartItem);
            }
            return shoppingCart;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }



    @Override
    public void delete(int userId) {
        String sql = "DELETE FROM shopping_cart " +
                " WHERE user_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public ShoppingCart create(int userId, int productId) {
        ShoppingCart shoppingCart = getByUserId(userId);

        ShoppingCartItem shoppingCartItem = shoppingCart.get(productId);

        String sql = "";
        if(shoppingCartItem == null) {
            sql = "INSERT INTO shopping_cart(user_id, product_id, quantity) " +
                    " VALUES (?, ?, 1);";
        } else {
            sql = "UPDATE shopping_cart SET quantity = ? WHERE product_id = ? and user_id = ?";
        }



        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            if(shoppingCartItem == null) {
                statement.setInt(1, userId);
                statement.setInt(2, productId);
            } else {
                statement.setInt(1, shoppingCartItem.getQuantity() + 1);
                statement.setInt(2, productId);
                statement.setInt(3, userId);
            }

            int rowsAffected = statement.executeUpdate();


        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return getByUserId(userId);
    }


    @Override
    public ShoppingCart update(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE product_id = ? AND user_id = ?";


        try (Connection connection = getConnection()) {
            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set parameters for each update
            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.setInt(3, userId);

            statement.addBatch();
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating shopping cart", e);
        }
        return null;
    }

    protected static Product mapRow(ResultSet row) throws SQLException
    {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
    }
}