package com.example.mailserver.mapper;

import com.example.mailserver.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "email", property = "email"),
            @Result(column = "password", property = "password"),
            @Result(column = "status", property = "status"),
            @Result(column = "role", property = "role")
    })
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    @Insert("INSERT INTO users (username, password, email, status, role) VALUES (#{username}, #{password}, #{email}, #{status}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(User user);

    @Select("SELECT * FROM users")
        @Results(value = {
            @Result(column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "email", property = "email"),
            @Result(column = "password", property = "password"),
            @Result(column = "status", property = "status"),
            @Result(column = "role", property = "role")
    })
    List<User> selectAll();
}
