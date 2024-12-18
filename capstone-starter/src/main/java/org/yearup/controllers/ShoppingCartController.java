package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController {
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping("")
    public ShoppingCart getCart(Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());

        // Use the HashMap to return all items
        return cart;
    }

    @PostMapping("/products/{productId}")
    public void addProductToCart(@PathVariable int productId, @RequestBody ShoppingCartItem cartItem, Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);

        Product product = productDao.getById(productId);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        }

        cartItem.setProduct(product);
        shoppingCartDao.addItem(user.getId(), cartItem);
    }

    @PutMapping("/products/{productId}")
    public void updateItemQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem cartItem, Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);

        if (cartItem.getQuantity() <= 0) {
            shoppingCartDao.removeItem(user.getId(), productId);
        } else {
            shoppingCartDao.updateItem(user.getId(), productId, cartItem.getQuantity());
        }
    }

    @DeleteMapping("/products/{productId}")
    public void removeItemFromCart(@PathVariable int productId, Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        shoppingCartDao.removeItem(user.getId(), productId);
    }

    @DeleteMapping("")
    public void clearCart(Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        shoppingCartDao.clearCart(user.getId());
    }
}
