package com.example.mailserver.mapper;

import com.example.mailserver.entity.Email;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmailMapper {
    @Select("SELECT * FROM emails WHERE receiver = #{receiver}")
    @Results({
            @Result(column = "email_id", property = "emailId"),
            @Result(column = "sender", property = "sender"),
            @Result(column = "receiver", property = "receiver"),
            @Result(column = "body", property = "body"),
            @Result(column = "sent_at", property = "sentAt"),
            @Result(column = "is_read", property = "isRead")
    })
    List<Email> getEmailsByReceiver(String receiver);

    @Insert("INSERT INTO emails (sender, receiver, body, sent_at, is_read) " +
            "VALUES (#{sender}, #{receiver}, #{body}, #{sentAt}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "emailId")
    boolean insertEmail(Email email);

    @Delete("DELETE FROM emails WHERE email_id = #{emailId}")
    boolean deleteById(int emailId);
}
