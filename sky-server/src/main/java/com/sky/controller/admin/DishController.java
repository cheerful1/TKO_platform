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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping()
    @ApiOperation(value = "保存菜品", notes = "保存菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("保存菜品:{}", dishDTO);
        dishService.saveWithFlavors(dishDTO);
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
        return Result.success();
    }







}
