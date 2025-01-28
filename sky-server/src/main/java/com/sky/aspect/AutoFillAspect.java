package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author : wangshanjie
 * @date : 18:10 2025/1/27
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void AutoFillPointCut() {}

    /**
     * 前置通知
     * 执行Insert、Update操作之前，自动填充公共字段
     */
    @Before("AutoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充");
        // 拦截了mapper的方法
        // 获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 获取到方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); // 获取到方法上的注解对象
        OperationType operationType = autoFill.value(); // 获取到注解中的操作类型
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || args == null) {
            return;
        }
        // 获取参数对象
        Object entity = args[0];

        // 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        long currebtId = BaseContext.getCurrentId();
        try {
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

            // 根据当前不同的数据库操作类型，调用不同的方法，比如：insert、update
             if (operationType == OperationType.INSERT) {
                 Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                 Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                 //为四个公共字段赋值
                 setCreateTime.invoke(entity,now);
                 setCreateUser.invoke(entity,currebtId);
                 setUpdateTime.invoke(entity,now);
                 setUpdateUser.invoke(entity,currebtId);
             }

             if (operationType == OperationType.UPDATE) {
                 // 为2个公共字段赋值
                     setUpdateTime.invoke(entity,now);
                     setUpdateUser.invoke(entity,currebtId);
             }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 后置通知
     */
    public void after() {
        log.info("after");
    }
}
