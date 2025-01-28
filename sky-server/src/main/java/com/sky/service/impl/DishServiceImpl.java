package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : wangshanjie
 * @date : 13:46 2025/1/28
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 保存菜品及其配料信息
     * @param dishDTO   菜品信息及其配料信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithFlavors(DishDTO dishDTO) {
        // 1.向菜品表保存数据（菜品）
        Dish dish = new Dish();
        // 复制属性,属性命名必须保持一致
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        // 保存成功后，获取菜品id
        Long dishId = dish.getId();

        // 2.口味表插入n条数据（菜品对应的口味）
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 判断是否存在口味数据
        if (flavors!= null && flavors.size() > 0) {
            flavors.forEach(flavor -> {
                // 设置菜品id
                flavor.setDishId(dishId);
            });
            // 批量插入口味数据
            dishFlavorMapper.insertBatch(flavors);
        }


    }
}
