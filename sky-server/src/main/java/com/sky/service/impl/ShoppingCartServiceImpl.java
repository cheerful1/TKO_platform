package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : wangshanjie
 * @date : 20:40 2025/2/2
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车信息
     */
    @Override
    public void addToCart(ShoppingCartDTO shoppingCartDTO) {
        // 1.判断当前加入到购物车的菜品是否已经在购物车中,根据套餐id和用户id判断
        ShoppingCart shoppingCart = new ShoppingCart();
        // 对象的属性拷贝
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        // 没有登录用户的id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list!= null && list.size() > 0){
            // 2.如果已经在购物车中，则数量+1
            ShoppingCart cart = list.get(0);// 取出第一个,唯一的这一个
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        }else{
            // 3.如果不在购物车中，则直接添加到购物车中
            shoppingCart.setNumber(1);
            // 菜品or套餐
            // 判断本次添加的菜品是否是套餐还是单品
            Long dishId = shoppingCartDTO.getDishId();
            if(dishId != null){
                // 本次添加到购物车的是菜品
                Dish dish = dishMapper.selectByPrimaryKey(dishId);
                shoppingCart.setDishId(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else {
                // 本次添加到购物车的是套餐
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);

                shoppingCart.setSetmealId(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    /**
     * 显示购物车
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        // 1.获取当前登录用户的id
        Long currentId = BaseContext.getCurrentId();
        // 2.根据用户id查询购物车信息
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(currentId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShoppingCart() {
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(currentId);
        shoppingCartMapper.delete(shoppingCart);
    }
}
