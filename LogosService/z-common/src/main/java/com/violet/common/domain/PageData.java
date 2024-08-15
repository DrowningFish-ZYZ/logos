package com.violet.common.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.ArrayList;

@Data
public class PageData {
    private Object data;
    private Long total;
    private Long pages;
    private Long size;
    private Long current;

    public PageData() {}

    public PageData(Object data, Page page) {
        this.loadData(data, page);
    }

    public void loadData(Object data, Page page) {
        this.setData(data);
        this.setPages(page.getPages());
        this.setSize(page.getSize());
        this.setCurrent(page.getCurrent());
        this.setTotal(page.getTotal());
    }


    public static class Builder {
        /**
         * 构建一个完整内容的分页数据对象
         * @param data 该页数据
         * @param page 该页内容
         * @return
         */
        public static PageData build(Object data, Page page) {
            PageData pageData = new PageData();
            pageData.setData(data);
            pageData.setPages(page.getPages());
            pageData.setSize(page.getSize());
            pageData.setCurrent(page.getCurrent());
            pageData.setTotal(page.getTotal());
            return pageData;
        }

        /**
         * 构建一个空的分页数据对象
         * @return
         */
        public static PageData build() {
            PageData pageData = new PageData();
            pageData.setData(new ArrayList<>());
            pageData.setSize(0L);
            pageData.setPages(0L);
            pageData.setCurrent(0L);
            pageData.setTotal(0L);
            return pageData;
        }
    }
}
