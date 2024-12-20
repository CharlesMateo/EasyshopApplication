package org.yearup.controllers;

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
@RequestMapping("cart")
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class ShoppingCartController {

    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    public ShoppingCartController(ProductDao productDao, UserDao userDao, ShoppingCartDao shoppingCartDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try {
            // get the currently logged-in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shopping cartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("products/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ShoppingCart addCart(Principal principal, @PathVariable int id) {
        User user = userDao.getByUserName(principal.getName());

        try {
            return shoppingCartDao.create(user.getId(), id);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


    @PutMapping("products/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ShoppingCart updateProductInCart(Principal principal, @PathVariable int productId, @RequestBody int quantity) {
        User user = userDao.getByUserName(principal.getName());

        try {
            shoppingCartDao.update(user.getId(), productId, quantity);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        return null;
    }


    @DeleteMapping()
    public ShoppingCart deleteCart(Principal principal) {
        User user = userDao.getByUserName(principal.getName());

        try {
            shoppingCartDao.delete(user.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        return new ShoppingCart();
    }

}