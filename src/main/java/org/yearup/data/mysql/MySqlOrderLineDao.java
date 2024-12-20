package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemsDao;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Component
public class MySqlOrderLineDao extends MySqlDaoBase implements OrderLineItemsDao {
    public MySqlOrderLineDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void createOrderLine(int orderId, int productId, BigDecimal salesPrice, int quantity, BigDecimal discount) {


        String sql = "INSERT INTO order_line_items(order_id, product_id, sales_price, quantity, discount) " +
                " VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.setBigDecimal(3, salesPrice);
            statement.setInt(4, quantity);
            statement.setBigDecimal(5, discount);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}