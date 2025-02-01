package com.sky.controller.admin;

/**
 * @author : wangshanjie
 * @date : 22:37 2025/1/27
 */

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping()
    @ApiOperation(value = "保存菜品", notes = "保存菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("保存菜品:{}", dishDTO);
        dishService.saveWithFlavors(dishDTO);
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * 获取菜品列表
     * @param dishPageQueryDTO
     * @return Result<PageResult>
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取菜品列表", notes = "获取菜品列表")
    public Result<PageResult> getDishList(DishPageQueryDTO dishPageQueryDTO) {
        log.info("获取菜品列表:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.getDishList(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品 批量
     * @param ids 菜品id
     * @return Result
     */
    @DeleteMapping
    @ApiOperation(value = "删除菜品", notes = "删除菜品")
    public Result deleteDish(@RequestParam List<Long> ids) {
        log.info("批量删除菜品:{}", ids);
        dishService.deleteBatch(ids);
        clearCache("dish_*");
        return Result.success();
    }
    /**
     * 获取菜品详情
     * @param id 菜品id
     * @return Result<DishVO>
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取菜品详情", notes = "获取菜品详情")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        log.info("获取菜品详情:{}", id);
        DishVO dishVO = dishService.getDishByIdWithFlavors(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品
     * @param dishDTO 菜品信息
     * @return Result
     */
    @PutMapping()
    @ApiOperation(value = "更新菜品", notes = "更新菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("更新菜品:{}", dishDTO);
        dishService.updateWithFlavors(dishDTO);
        clearCache("dish_*");
        return Result.success();
    }
    /**
     * 启用或禁用菜品
     * @param status 状态
     * @param id 菜品id
     * @return Result<String>
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启用或禁用菜品", notes = "启用或禁用菜品")
    public Result<String> startOrStop(@PathVariable Integer status, Long id){
        log.info("更改菜品状态:{}", id);
        dishService.startOrStop(status, id);
        clearCache("dish_*");
        return Result.success();
    }


    /**
     * 清除缓存
     * @param patten 缓存key的通配符
     * @return Result
     */
    private void clearCache(String patten) {
        log.info("清除缓存:{}", patten);
        Set keys = redisTemplate.keys(patten);
        redisTemplate.delete(keys);

    }



}
