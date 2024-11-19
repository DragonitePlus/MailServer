package com.example.mailserver.mapper;

import com.example.mailserver.entity.Email;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface EmailMapper {
    @Select("SELECT * FROM emails WHERE receiver = #{receiver}")
    List<Email> getEmailsByReceiver(String receiver);

    @Insert("INSERT INTO emails (sender, receiver, body, sent_at, is_read) " +
            "VALUES (#{sender}, #{receiver}, #{body}, #{sentAt}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "emailId")
    boolean insertEmail(Email email);
}
