package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : wangshanjie
 * @date : 18:33 2025/1/28
 */
@Mapper
public interface SetmealDishMapper {


    /**
     * 根据菜品id列表获取套餐id列表
     * @param dishIds
     * @return
     */
    List<Long> getSetmealDishIdsByDishIds(List<Long> dishIds);
}
