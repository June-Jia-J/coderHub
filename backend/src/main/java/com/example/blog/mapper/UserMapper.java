package com.example.blog.mapper;

import com.example.blog.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT id, username, password_hash AS passwordHash, created_at AS createdAt FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    @Insert("INSERT INTO user (username, password_hash) VALUES (#{username}, #{passwordHash})")
    int insert(User user);
}
