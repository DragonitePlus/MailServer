package com.example.mailserver.mapper;

import com.example.mailserver.entity.Log;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LogsMapper {
    @Select("SELECT * FROM logs")
    @Results(value = {
            @Result (column = "log_id", property = "logId"),
            @Result (column = "type", property = "type"),
            @Result (column = "content", property = "content"),
            @Result (column = "created_time", property = "createdTime")
    })
    List<Log> getLog();

    @Insert("INSERT INTO logs (type, content, created_time) VALUES (#{type}, #{content}, #{createdTime})")
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    boolean insertLog(Log log);
}
