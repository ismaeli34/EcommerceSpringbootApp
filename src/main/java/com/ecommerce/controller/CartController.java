package com.ecommerce.controller;

import com.ecommerce.dao.CartDao;
import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartDao cartDao;

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/addToCart/{productId}"})
    public Cart addToCart(@PathVariable(name = "productId") Integer productId){
       return cartService.addToCart(productId);
    }

    @PreAuthorize(("hasRole('User')"))
    @GetMapping({"/getCartDetails"})
    public List<Cart> getCartDetails(){
        return cartService.getCartDetails();
    }

    @PreAuthorize(("hasRole('User')"))
    @DeleteMapping({"/deleteCartItem/{cartId}"})
    public void deleteCartItem(@PathVariable(name = "cartId") Integer cartId){
        cartService.deleteCartItem(cartId);
    }
}
