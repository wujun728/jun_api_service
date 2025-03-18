package io.github.wujun728.api.common.utils;

import io.github.wujun728.api.common.enums.EnumCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化工具类
 * @version 1.0
 * @date 2024-05-06
 */
@Component("lowCodeMessageUtils")
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey) {
        try {
            return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return msgKey;
        }
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(EnumCode enumCode) {
        try {
            return messageSource.getMessage(String.valueOf(enumCode.getCode()), null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return String.valueOf(enumCode.getCode());
        }
    }
}
