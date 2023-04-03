package com.ecommerce.service;

import com.ecommerce.configuration.JwtRequestFilter;
import com.ecommerce.dao.CartDao;
import com.ecommerce.dao.OrderDetailDao;
import com.ecommerce.dao.ProductDao;
import com.ecommerce.dao.UserDao;
import com.ecommerce.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {

    private static final String ORDER_PLACED ="Placed";

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private CartDao cartDao;


    public List<OrderDetail> getAllOrderDetails(String status){
        List<OrderDetail> orderDetails = new ArrayList<>();
        if (status.equals("All")){
            orderDetailDao.findAll().forEach(x->orderDetails.add(x));

        }else{
            orderDetailDao.findByOrderStatus(status).forEach(
                    x ->orderDetails.add(x)
            );
        }

        return orderDetails;
    }

    public List<OrderDetail> getOrderDetails(){

        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userDao.findById(currentUser).get();
        List<OrderDetail> orderDetailList = orderDetailDao.findByUser(user);

        return orderDetailList;
    }

    public void  placeOrder(OrderInput orderInput,boolean isSingleProductCheckout){

        List<OrderProductQuantity> list = orderInput.getOrderProductQuantityList();
        for (OrderProductQuantity o: list){
            Product product = productDao.findById(o.getProductId()).get();
            String currentUser = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(currentUser).get();

            OrderDetail orderDetail = new OrderDetail(
                    orderInput.getFullName(),
                    orderInput.getFullAddress(),
                    orderInput.getContactNumber(),
                    orderInput.getAlternateContactNumber(),
                    ORDER_PLACED,
                    product.getProductDiscountedPrice() * o.getQuantity(),
                    product,
                    user
            );

            //empty the cart.
            if(!isSingleProductCheckout){
                List<Cart> carts = cartDao.findByUser(user);
                carts.stream().forEach(x->cartDao.deleteById(x.getCartId()));
            }

            orderDetailDao.save(orderDetail);
        }

    }

    public void markOrderAsDelivered(Integer orderId){

        OrderDetail orderDetail = orderDetailDao.findById(orderId).get();
        if(orderDetail!=null){
            orderDetail.setOrderStatus("Delivered");
            orderDetailDao.save(orderDetail);
        }


    }
}
