package com.example.blog.mapper;

import com.example.blog.entity.Blog;
import com.example.blog.dto.BlogListVO;
import com.example.blog.dto.BlogVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {

    @Insert("INSERT INTO blog (user_id, title, content) VALUES (#{userId}, #{title}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Blog blog);

    BlogVO selectById(@Param("id") Long id);

    int update(Blog blog);

    int deleteById(@Param("id") Long id);

    Long selectUserIdById(@Param("id") Long id);

    List<BlogListVO> selectPage(@Param("offset") int offset, @Param("limit") int limit, @Param("author") String author);

    int count(@Param("author") String author);
}
