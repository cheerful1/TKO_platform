package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.IteratorUtils.forEach;

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

    @Autowired
    private SetmealDishMapper setmealDishMapper;
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

    /**
     *  分页查询菜品列表
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult getDishList(DishPageQueryDTO dishPageQueryDTO) {
        // 1.分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.selectByPage(dishPageQueryDTO);
        long total = dishPage.getTotal();
        List<DishVO> dishes = dishPage.getResult();
        return  new PageResult(total, dishes);
    }


    /**
     * 批量删除菜品
     * @param ids 菜品id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        // 1.判断当前菜品是否能够删除--是否存在起售中的菜品
        ids.forEach(id -> {
            Dish dish = dishMapper.selectByPrimaryKey(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });

        //2.判断当前菜品是否能够删除--是否存在套餐中

        List<Long> setmealIds = setmealDishMapper.getSetmealDishIdsByDishIds(ids);
        if (setmealIds!= null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //3.删除菜品数据
//        ids.forEach(id -> {
//            dishMapper.deleteByPrimaryKey(id);
//            //4.删除菜品对应的口味数据
//            dishFlavorMapper.deleteByDishId(id);
//        });
        //优化：批量删除菜品对应的菜品
        dishMapper.deleteBatch(ids);

        //优化：批量删除菜品对应的口味数据
        dishFlavorMapper.deleteBatchByDishIds(ids);

    }

    /**
     * 根据id查询菜品及其配料信息
     * @param id 菜品id
     * @return 菜品及其配料信息
     */
    @Override
    public DishVO getDishByIdWithFlavors(Long id) {
            // 1.查询菜品信息
        Dish dish = dishMapper.selectByPrimaryKey(id);
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 更新菜品及其配料信息
     * @param dishDTO 菜品及其配料信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithFlavors(DishDTO dishDTO) {
        // 1.菜品表更新
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateByPrimaryKeySelective(dish);

        // 先删除原有的口味数据
        dishFlavorMapper.deleteByDishId(dish.getId());

        // 2.口味表更新
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishDTO.getId());
            });
            // 再插入新的口味数据
            dishFlavorMapper.insertBatch(flavors);
        }




    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        // 1.查询菜品列表
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 启用或禁用菜品
     * @param status 状态
     * @param id 菜品id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.updateByPrimaryKeySelective(dish);    //更新状态
    }
}
