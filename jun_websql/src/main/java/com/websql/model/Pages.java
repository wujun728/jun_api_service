package com.websql.model;


import lombok.Data;

/**
 * @program: Pages
 * @description: 分页实体
 * @author: rabbit boy_0214@sina.com
 * @create: 2019-09-19 10:00
 **/
@Data
public class Pages {

    private int page;

    private int limit;

    private String token;

    public int getPage() {
        if (page < 0 || page == 0) {
            page = 1;
        }
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        if (limit < 0 || limit == 0) {
            limit = 10;
        }
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
