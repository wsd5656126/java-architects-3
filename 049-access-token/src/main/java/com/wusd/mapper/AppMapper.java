package com.wusd.mapper;

import com.wusd.entity.App;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AppMapper {
    @Select("SELECT id, appId, appName, appSecret, accessToken, isFlag FROM app " +
            "WHERE appId = #{appId} and appSecret=#{appSecret}")
    App findApp(App app);

    @Select("SELECT id, appId, appName, appSecret, accessToken, isFlag FROM app WHERE appId = #{appId}")
    App findAppId(@Param("appId") String appId);

    @Update("UPDATE app SET accessToken = #{accessToken} WHERE appId = #{appId}")
    int updateAccessToken(@Param("accessToken") String accessToken, @Param("appId") String appId);
}
