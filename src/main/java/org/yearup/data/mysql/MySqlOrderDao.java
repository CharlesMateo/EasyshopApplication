package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Order;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
@Component
public class MySqlOrderDao  extends MySqlDaoBase implements OrderDao {
    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int checkout(int userId, LocalDateTime date, String address, String city, String state, String zip, BigDecimal shippingAmount){

        String sql = "INSERT INTO orders(user_id, date, address, city, state, zip, shipping_amount) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setString(2, date.toString());
            statement.setString(3, address);
            statement.setString(4, city);
            statement.setString(5, state);
            statement.setString(6, zip);
            statement.setBigDecimal(7, shippingAmount);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);  // Return the generated orderId
                    }
                }
            } else {
                // Return 0 or handle a failure case if no rows were inserted
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception for debugging
            throw new RuntimeException("Error occurred while processing the order checkout", e);
        }

        return 0;  // Return 0 if insertion failed or no rows were inserted
    }

}