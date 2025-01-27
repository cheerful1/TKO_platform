package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : wangshanjie
 * @date : 18:05 2025/1/27
 */
// 注解类 用于标记需要自动填充的字段，一般是加在方法上面
@Target(ElementType.METHOD)
// 注解的生命周期为运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 注解属性，用于指定需要自动填充的字段
    OperationType value();

}
