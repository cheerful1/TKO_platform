package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import static java.lang.String.valueOf;

/**
 * @author : wangshanjie
 * @date : 22:28 2025/1/28
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String SHOP_STATUS = "ShopStatus";

    @GetMapping("/status")
    @ApiOperation(value = "获取店铺状态")
    public Result<Integer> getStatus() {
        // 从redis中获取店铺状态
        String status = (String) redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("获取店铺状态，status={}", status.equals("1")? "营业中" : "打烊中");
        return Result.success(status.equals("1")? 1 : 0);

    }

}
