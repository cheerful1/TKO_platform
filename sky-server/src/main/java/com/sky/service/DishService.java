package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author : wangshanjie
 * @date : 13:46 2025/1/28
 */
public interface DishService {

    /**
     * 保存菜品信息
     * @param dishDTO
     */
    void saveWithFlavors(DishDTO dishDTO);

    /**
     *  分页查询菜品列表
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult getDishList(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
