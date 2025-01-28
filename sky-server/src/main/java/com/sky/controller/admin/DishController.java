package com.sky.controller.admin;

/**
 * @author : wangshanjie
 * @date : 22:37 2025/1/27
 */

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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





}
