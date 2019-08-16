package com.wusd.mapper2;

import com.wusd.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper2 {
    @Select("select * from user where userName=#{0}")
    User findUserByName(String userName);
}
