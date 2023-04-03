package com.ecommerce.service;

import com.ecommerce.configuration.JwtRequestFilter;
import com.ecommerce.dao.CartDao;
import com.ecommerce.dao.ProductDao;
import com.ecommerce.dao.UserDao;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CartDao cartDao;

    public Product addNewProduct(Product product){
       return productDao.save(product);
    }

    public List<Product> getAllProducts(int pageNumber,String searchKey){
        Pageable pageable = PageRequest.of(pageNumber,5);
        if (searchKey.equals("")) {
            return (List<Product>) productDao.findAll(pageable);

        }else{
         return  (List<Product>) productDao.findByProductNameContainingIgnoreCase(
                    searchKey,pageable
            );
        }


    }

    public void deleteProduct(Integer productId){
        this.productDao.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        productDao.deleteById(productId);
    }

    public Product getProductDetailsById(Integer productId){

       return this.productDao.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
    }
    
    public List<Product> getProductDetails(boolean isSingleProductCheckout, Integer productId){
        if(isSingleProductCheckout && productId !=0){
            //we are going to buy single product
            ArrayList<Product> list = new ArrayList<>();
            Product product = productDao.findById(productId).get();
            list.add(product);
            return list;
        }else{
            //we are going to checkout entire cart
            String username = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(username).get();
            List<Cart> carts = this.cartDao.findByUser(user);
            List<Product> productList = carts.stream().map(x -> x.getProduct()).collect(Collectors.toList());
            return productList;


        }
    }
}
