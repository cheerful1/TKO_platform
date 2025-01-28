package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


/**
 * @author : wangshanjie
 * @date : 22:28 2025/1/28
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String SHOP_STATUS = "ShopStatus";

    @PutMapping("/{status}")
    @ApiOperation(value = "设置店铺状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺状态，id={}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(SHOP_STATUS, String.valueOf(status));
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation(value = "获取店铺状态")
    public Result<Integer> getStatus() {
        // 从redis中获取店铺状态
//        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        String status = (String) redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("获取店铺状态，status={}", status.equals("1") ? "营业中" : "打烊中");
        return Result.success(Integer.parseInt(status));

    }

}
