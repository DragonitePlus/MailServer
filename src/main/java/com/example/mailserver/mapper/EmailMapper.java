package com.example.mailserver.mapper;

import com.example.mailserver.entity.Email;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface EmailMapper {
    @Select("SELECT * FROM emails WHERE receiver = #{receiver}")
    List<Email> getEmailsByReceiver(String receiver);
}
