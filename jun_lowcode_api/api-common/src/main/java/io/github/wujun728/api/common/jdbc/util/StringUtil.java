package io.github.wujun728.api.common.jdbc.util;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String toLowerCaseFirstOne(String str) {
        return Character.isLowerCase(str.charAt(0)) ? str : Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String toUpperCaseFirstOne(String str) {
        return Character.isUpperCase(str.charAt(0)) ? str : Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 脱敏处理
     * @param str
     * @return
     */
    public static String getDesensiStr(String str) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        String res = str.substring(0,1);
        for (int i=0;i<str.length()-2;i++){
            res += "*";
        }
        res += str.substring(str.length()-1);
        return res;
    }

    public static String parsePackName(String path) {
        StringBuffer sb = new StringBuffer();
        String[] arr = null;
        if (path.indexOf("/") != -1) {
            arr = path.split("/");
        }

        if (path.indexOf("\\") != -1) {
            arr = path.split("\\\\");
        }

        sb.append(arr[1]);

        for(int i = 2; i < arr.length; ++i) {
            sb.append("." + arr[i]);
        }

        return sb.toString();
    }

    public static String toUnderscoreToCamelCase(String str) {
        String camelStr = "";
        if (StrUtil.isNotBlank(str)) {
            String[] items = str.split("_");

            for(int i = 0; i < items.length; ++i) {
                if (i == 0) {
                    camelStr = camelStr + toLowerCaseFirstOne(items[i].toLowerCase());
                } else {
                    camelStr = camelStr + toUpperCaseFirstOne(items[i].toLowerCase());
                }
            }
        }

        return camelStr;
    }

    public static String strToCharAndQuot(String str) {
        String newStr = "";
        if (StrUtil.isNotBlank(str)) {
            String[] array = str.split(",");

            for(int i = 0; i < array.length; ++i) {
                if(array[i].indexOf("'")!=-1){
                    continue;
                }
                if (i == 0) {
                    newStr = newStr + "'" + array[i] + "'";
                } else {
                    newStr = newStr + ",'" + array[i] + "'";
                }
            }
        }

        return newStr;
    }

    public static String array2CharStr(String ... array) {
        String newStr = "";
        if (array != null && array.length > 0) {
            for(int i = 0; i < array.length; ++i) {
                if(array[i].indexOf("'")!=-1){
                    continue;
                }
                if (i == 0) {
                    newStr = newStr + "'" + array[i] + "'";
                } else {
                    newStr = newStr + ",'" + array[i] + "'";
                }
            }
        }

        return newStr;
    }

    public static String array2Char(List<Object> list) {
        String newStr = "";
        if (list != null && list.size() > 0) {
            for(int i = 0; i < list.size(); ++i) {
                if(list.get(i) instanceof String){
                    if(((String)list.get(i)).indexOf("'")!=-1){
                        continue;
                    }
                    if (i == 0) {
                        newStr += "'" + (String)list.get(i) + "'";
                    } else {
                        newStr += ",'" + (String)list.get(i) + "'";
                    }
                }else if(list.get(i) instanceof Integer){
                    if (i == 0) {
                        newStr += ""+(Integer)list.get(i);
                    } else {
                        newStr += "," + (Integer)list.get(i);
                    }
                }
            }
        }

        return newStr;
    }

    public static String array2CharInt(Integer ... array) {
        return ArrayUtil.join(array,",");
    }

    public static List<String> getImgStr(String htmlStr) {
        List<String> list = new ArrayList();
        String img = "";
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        String regEx_src = "src\\s*=\\s*\"?(.*?)(\"|>|\\s+)";
        Pattern p_image = Pattern.compile(regEx_img, 2);
        Matcher m_image = p_image.matcher(htmlStr);

        while(m_image.find()) {
            img = m_image.group();
            Matcher m = Pattern.compile(regEx_src).matcher(img);

            while(m.find()) {
                list.add(m.group(1));
            }
        }

        return list;
    }

    public static String getValidNullStr(String str) {
        return str == null ? "" : str;
    }

    public static String getLinkPath(String server, String path) {
        return path=="" || path == null?"":(server+path);
    }

    public static String getValidNullStr(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String getValidNullStr(Integer i) {
        return i == null ? "" : i+"";
    }

    public static String getListItemStr(List<Object> list, int index) {
        return list != null && list.size() > 0 && list.size() > index ? getValidNullStr(list.get(index)) : "";
    }

    public static String getParamInBracket(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        } else {
            String reg = "\\$\\{(.+)\\}";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(str);
            return m.matches() ? str.substring(str.indexOf("{") + 1, str.indexOf("}")) : null;
        }
    }

    public static int indexOfArray(String[] array, String value) {
        if (array != null && array.length > 0 && !StrUtil.isBlank(value)) {
            int index = -1;

            for(int i = 0; i < array.length; ++i) {
                if (value.equals(array[i])) {
                    index = i;
                    break;
                }
            }

            return index;
        } else {
            return -1;
        }
    }

    /**
     * 驼峰命名 转换为下划线
     * @param str
     * @return
     */
    public static String camelToSnake(String str){
        StringBuffer sb = new StringBuffer();
        if (StrUtil.isBlank(str)) {
            return "";
        }
        for (int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if(Character.isUpperCase(c)){
                if(i==0){
                    sb.append(c);
                }else{
                    sb.append("_").append(Character.toLowerCase(c));
                }
            }else {
                sb.append(c);
            }

        }
        return sb.toString();
    }

    /**
     * UTC时间转换
     * 2024-04-01T12:10:01.000Z
     * @param datetime
     * @return
     */
    public static String parseUtcTime(String datetime){
        if(StrUtil.isEmpty(datetime)){
            return datetime;
        }
        Instant instant = Instant.parse(datetime);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.format(DatePattern.NORM_DATETIME_FORMATTER);
    }
}
