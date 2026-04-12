package com.example.blog.dto;

import java.util.List;

public class BlogPageVO {
    private long total;
    private List<BlogListVO> list;

    public BlogPageVO(long total, List<BlogListVO> list) {
        this.total = total;
        this.list = list;
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public List<BlogListVO> getList() { return list; }
    public void setList(List<BlogListVO> list) { this.list = list; }
}
