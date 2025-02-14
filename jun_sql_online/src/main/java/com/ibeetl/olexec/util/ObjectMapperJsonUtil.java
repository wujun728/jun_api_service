package com.ibeetl.olexec.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beetl.ext.simulate.JsonUtil;

public class ObjectMapperJsonUtil implements JsonUtil {
    ObjectMapper objMapper;

    public ObjectMapperJsonUtil(ObjectMapper objMapper) {
        this.objMapper = objMapper;
    }

    public String toJson(Object o) throws Exception {
        return this.objMapper.writeValueAsString(o);
    }

    public Object toObject(String str, Class c) throws Exception {
        return this.objMapper.readValue(str, c);
    }
}
