package io.github.wujun728.api.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;

/**
 * 对象工具类
 *
 * @version 1.0
 * @date 2024-05-06
 */
public class BeanUtil {

    /**
     * bean转换map
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean) {
        return beanToMap(bean, true);
    }

    /**
     * bean转换map
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean, boolean ignoreEmptyValue) {
        Map<String, Object> map = new HashMap<>();
        if (bean == null) {
            return map;
        }
        map = cn.hutool.core.bean.BeanUtil.beanToMap(bean);
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            if (ignoreEmptyValue) {
                if (next.getValue() == null || StrUtil.isBlank(next.getValue().toString())) {
                    iterator.remove();
                }
            } else {
                if (next.getValue() == null) {
                    iterator.remove();
                }
            }
        }
        return map;
    }

    public static  <T> T copy(Object source, Class<T> target) throws InstantiationException, IllegalAccessException {
        if(source==null){
            return null;
        }
        T t = target.newInstance();
        cn.hutool.core.bean.BeanUtil.copyProperties(source, t);
        return t;
    }

    public static  <S,T> List<T> parseList(List<S> sources, Class<T> targets) throws InstantiationException, IllegalAccessException {
        if(ObjectUtil.isEmpty(sources)){
            return new ArrayList<T>();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S s:sources){
            T t = targets.newInstance();
            cn.hutool.core.bean.BeanUtil.copyProperties(s, t);
            list.add(t);
        }
        return list;
    }
}
