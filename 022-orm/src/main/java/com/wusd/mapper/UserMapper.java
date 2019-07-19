package com.wusd.mapper;

import com.wusd.entity.po.User;
import com.wusd.orm.annotation.Insert;
import com.wusd.orm.annotation.Param;
import com.wusd.orm.annotation.Select;

public interface UserMapper {
    @Insert("insert into user(userId, userName, age) values(#{userId},#{userName},#{age})")
    int insert(@Param("userId") String userId, @Param("userName") String userName, @Param("age") Integer age);

    @Select("select * from User where userName=#{userName} and userAge=#{age}")
    User select(@Param("userName") String userName, @Param("userAge") Integer userAge);
}
