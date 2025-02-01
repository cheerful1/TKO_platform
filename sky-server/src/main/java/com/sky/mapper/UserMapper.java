package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author : wangshanjie
 * @date : 15:11 2025/1/29
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User getUserById(String openid);

    /**
     * 插入用户信息
     * @param userEntity
     */
    void insert(User userEntity);
}
