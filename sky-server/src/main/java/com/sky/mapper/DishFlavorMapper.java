package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : wangshanjie
 * @date : 14:31 2025/1/28
 */
@Mapper
public interface DishFlavorMapper {
    /**`
     * 插入菜品口味
     * @param  flavors 菜品口味
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除菜品口味
     * @param id
     */
    @Delete("DELETE FROM dish_flavor WHERE dish_id = #{id}")
    void deleteByDishId(Long id);


    /**
     * 根据菜品id批量删除菜品口味
     * @param dishIds 菜品id列表
     */
    void deleteBatchByDishIds(List<Long> dishIds);
}
