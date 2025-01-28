package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 * @author : wangshanjie
 * @date : 13:19 2025/1/28
 */
// Configuration注解表示这是一个配置类，可以被Spring Boot扫描到并加载。
@Configuration
@Slf4j
public class OssConfiguration {
    // 定义Bean，实例化AliOssUtil。
    @Bean
    @ConditionalOnMissingBean
    // ConditionalOnMissingBean注解表示只有在不存在Bean的情况下，才会实例化AliOssUtil。
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("初始化阿里云OSS工具类: {}",aliOssProperties);
        AliOssUtil aliOssUtil = new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
        return aliOssUtil;
    }
}
