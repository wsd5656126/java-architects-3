package com.wusd.mapper;

import com.wusd.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("SELECT * from user WHERE userName = #{0}")
    User findUserByName(String userName);

    @Insert("INSERT INTO user(userId, userName, age) VALUES (#{0}, #{1}, #{2})")
    int insert(String userId, String userName, Integer age);
}
