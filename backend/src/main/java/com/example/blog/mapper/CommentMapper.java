package com.example.blog.mapper;

import com.example.blog.entity.Comment;
import com.example.blog.dto.CommentVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment (blog_id, user_id, content) VALUES (#{blogId}, #{userId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    CommentVO selectById(@Param("id") Long id);

    List<CommentVO> selectByBlogId(@Param("blogId") Long blogId);
}
