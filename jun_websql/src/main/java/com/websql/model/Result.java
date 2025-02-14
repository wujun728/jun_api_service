package com.websql.model;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Result
 * @Description 分页BEAN
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 17:58
 **/
@Data
public class Result<T> {

    int page;
    int limit;
    int count;
    String code;
    String msg;
    List<T> list;
    T example;

}
