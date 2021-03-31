package com.hanil.fluxus.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper {

    Map<String, Object> getLoginInfo(Map<String, Object> inputs);

    int insertUserJoin(Map<String, Object> inputs);

    int setTempKey(Map<String, Object> inputs);
}
