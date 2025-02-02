package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author : wangshanjie
 * @date : 20:39 2025/2/2
 */
public interface ShoppingCartService {
    /**
     *  添加购物车
     * @param shoppingCartDTO 购物车信息
     */
    void addToCart(ShoppingCartDTO shoppingCartDTO);


    List<ShoppingCart> showShoppingCart();

    /**
     *  清空购物车
     */
    void cleanShoppingCart();
}
