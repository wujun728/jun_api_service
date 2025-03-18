package io.github.wujun728.api.common.config;

import io.github.wujun728.api.common.utils.ConstantUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 处理LocaleResolver
 * @version 1.0
 * @date 2024-05-06
 */
public class MyLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String lang = request.getHeader(ConstantUtil.HEADER_PARAM_LANGUAGE);
        if(StrUtil.isBlank(lang)){
            lang = request.getParameter(ConstantUtil.HEADER_PARAM_LANGUAGE);
        }
        lang  = StrUtil.isBlank(lang)?ConstantUtil.LANGUAGES[0]:lang;
        if(!ListUtil.toList(ConstantUtil.LANGUAGES).contains(lang)){
            lang = ConstantUtil.LANGUAGES[0];
        }
        Locale locale = Locale.getDefault();
        if (lang.split("_").length == 2) {
            String[] split = lang.split("_");
            locale = new Locale(split[0], split[1]);
        }else{
            lang = ConstantUtil.LANGUAGES[0];
            String[] split = lang.split("_");
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
