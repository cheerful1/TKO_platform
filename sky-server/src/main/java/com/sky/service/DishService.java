package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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
    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    DishVO getDishByIdWithFlavors(Long id);

    /**
     * 更新菜品信息
     * @param dishDTO
     */
    void updateWithFlavors(DishDTO dishDTO);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 启用或禁用菜品
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
