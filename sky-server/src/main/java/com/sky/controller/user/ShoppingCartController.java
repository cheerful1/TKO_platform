package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : wangshanjie
 * @date : 20:33 2025/2/2
 */
@RestController("ShoppingCartController")
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端购物车相关接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation(value = "添加购物车")
    public Result addToCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        // 业务逻辑
        log.info("添加购物车，商品信息：{}", shoppingCartDTO);
        shoppingCartService.addToCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 获取购物车列表
     *
     * @return 购物车列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取购物车列表")
    public Result<List<ShoppingCart>> list() {
        // 业务逻辑
        log.info("获取购物车列表");
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    @ApiOperation(value = "删除购物车商品")
    public Result clean() {
        // 业务逻辑
        log.info("删除购物车商品");
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }


}
