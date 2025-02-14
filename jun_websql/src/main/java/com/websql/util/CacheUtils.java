package com.websql.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.ObjectUtil;

/**
 * @ClassName : CacheUtils
 * @Description : 缓存工具类
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/29 14:24
 */
public class CacheUtils {

    private static final Integer TIME_OUT = 1000 * 60 * 60 * 8;

    private static final Integer SCHEDULE_TIME = 1000 * 60 * 30;


    private static final TimedCache<String, Object> localCache = CacheUtil.newTimedCache(Integer.MAX_VALUE);


    static {
        localCache.schedulePrune(SCHEDULE_TIME);
    }

    public static void put(String key, Object value) {
        put(key, value, TIME_OUT);
    }

    public static void putNoDue(String key, Object value) {
        localCache.put(key, value);
    }

    public static void put(String key, Object value, Integer time) {
        localCache.put(key, value, time);
    }

    public static Object get(String key) {
        return get(key, Object.class);
    }

    public static <T> T get(String key, Class<T> value) {
        return (T) localCache.get(key);
    }

    public static Boolean exist(String key) {
        if (ObjectUtil.isNotNull(localCache.get(key))) {
            return true;
        }
        return false;
    }

    public static void remove(String key) {
        localCache.remove(key);
    }

}
