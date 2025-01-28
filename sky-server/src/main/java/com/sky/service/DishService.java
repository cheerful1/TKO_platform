package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;

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
}
