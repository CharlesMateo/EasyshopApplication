package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.data.mysql.MySqlShoppingCartDao;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

@RestController
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
@RequestMapping("cart")
public class OrderController {

    private OrderDao orderDao;
    private UserDao userDao;
    private ProfileDao profileDao;
    private ShoppingCartDao shoppingCartDao;
    private OrderLineItemsDao orderLineItemsDao;
    private ProductDao productDao;

    public OrderController(OrderDao orderDao, UserDao userDao, ProfileDao profileDao, ShoppingCartDao shoppingCartDao,
                           OrderLineItemsDao orderLineItemsDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.shoppingCartDao = shoppingCartDao;
        this.orderLineItemsDao = orderLineItemsDao;
        this.productDao = productDao;
    }

    @PostMapping
    public ResponseEntity<Order> checkout(Principal principal){
        User user = userDao.getByUserName(principal.getName());
        try
        {
            var profile = profileDao.getByUserId(user.getId());
            var shoppingCart = shoppingCartDao.getByUserId(user.getId());



            int orderId = orderDao.checkout(user.getId(), LocalDateTime.now(), profile.getAddress(), profile.getCity(),
                    profile.getState(), profile.getZip(), shoppingCart.getTotal());
            // TODO: make orderline
            /*
             *  need:
             *  1. order id
             *  2. product id
             *  3. sales price
             *  4. quantity
             *  5. discount
             *
             * */

            for (ShoppingCartItem shoppingCartItem : shoppingCart.getItems().values()) {
                int productId = shoppingCartItem.getProductId(); // 2
                Product product = productDao.getById(productId); // Retrieve product by productId
                BigDecimal productPrice = product.getPrice(); // 3
                int quantity = shoppingCartItem.getQuantity(); // 4
                BigDecimal discount = BigDecimal.ZERO; // Default discount

                // Create an order line for each item in the cart
                orderLineItemsDao.createOrderLine(orderId, productId, productPrice, quantity, discount);
            }


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        return ResponseEntity.ok().build();
    }

}