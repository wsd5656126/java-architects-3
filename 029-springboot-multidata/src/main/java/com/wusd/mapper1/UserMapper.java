package com.wusd.mapper1;

import com.wusd.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select * from user where userName=#{0}")
    User findUserByName(String name);
}
