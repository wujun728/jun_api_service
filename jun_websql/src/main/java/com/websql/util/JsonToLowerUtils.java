package com.websql.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName JsonToLowerUtils
 * @Description json key全部转为小写
 * @Author rabbit boy_0214@sina.com
 * @Date 2024-10-14 12:42
 **/
public class JsonToLowerUtils {
    public static JSONObject transToLowerObject(String json) {
        JSONObject JSONObject2 = new JSONObject();
        JSONObject JSONObject1 = JSON.parseObject(json);
        for (String key : JSONObject1.keySet()) {
            Object object = JSONObject1.get(key);
            if (object.getClass().toString().endsWith("JSONObject")) {
                JSONObject2.put(key.toLowerCase(), transToLowerObject(object.toString()));
            } else {
                JSONObject2.put(key.toLowerCase(), object);
            }
        }
        return JSONObject2;
    }
}
