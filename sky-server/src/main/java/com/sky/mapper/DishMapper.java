package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     *  分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> selectByPage(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish selectByPrimaryKey(Long id);


    @Delete("delete from dish where id = #{id}")
    void deleteByPrimaryKey(Long id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 更新
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void updateByPrimaryKeySelective(Dish dish);
}
