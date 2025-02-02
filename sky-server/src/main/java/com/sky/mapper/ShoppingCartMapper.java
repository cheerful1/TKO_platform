package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author : wangshanjie
 * @date : 21:14 2025/2/2
 */
@Mapper
public interface ShoppingCartMapper {
    /**
     * 动态查询
     * @param shoppingCart 条件
     * @return 结果集
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     *  xin添加购物车
     *
     * @param cart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart cart);

    /**
     * xin添加购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) values(#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * xin删除购物车
     * @param shoppingCart
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void delete(ShoppingCart shoppingCart);
}
