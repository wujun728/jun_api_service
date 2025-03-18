package io.github.wujun728.api.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.StrUtil;
import org.hibernate.validator.internal.properties.Field;

import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @version 1.0
 * @date 2019-06-30
 **/
public class FileUtil {


    /**
     * 替换文件中的内容，复制文件
     * @param srcFilePath
     * @param destFilePath
     * @param map
     * @param override 是否覆盖 默认否
     */
    public static void copyFile(String srcFilePath, String destFilePath, Map<String,String> map, boolean override){
        boolean flag = false;
        File file = new File(destFilePath);
        file.getParentFile().mkdirs();
        if (file.exists()) {
            if(override){
                //覆盖，删除文件
                if(!srcFilePath.equals(destFilePath)){
                    file.delete();
                }else{
                    srcFilePath = srcFilePath+"js";
                    file.renameTo(new File(srcFilePath));
                    flag = true;
                }
            }else{
                System.out.println("warning message： " + destFilePath + " 已存在");
                return;
            }
        }

        System.out.println(DateUtil.now() + " " + destFilePath);
        try {
            FileReader reader = new FileReader(srcFilePath);
            BufferedReader bufferedReader = new BufferedReader(reader);

            File outFile = new File(destFilePath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            FileWriter writer = new FileWriter(outFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                line = replaceStrByMap(line, map);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            writer.close();
            reader.close();
            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(flag==true){
            new File(srcFilePath).delete();
        }
    }

    /**
     * 替换文件中的内容，复制文件
     * @param srcFilePath
     * @param destFilePath
     * @param map
     */
    public static void copyFile(String srcFilePath, String destFilePath, Map<String,String> map){
        copyFile(srcFilePath,destFilePath,map,false);
    }

    private static String replaceStrByMap(String str,Map<String,String> map){
        Matcher m= PatternPool.get("\\$\\{(\\w+)\\}").matcher(str);
        while(m.find()){
            String group = m.group();
            if(map.containsKey(m.group(1))){
                String value = StrUtil.isBlank(map.get(m.group(1)))?"":map.get(m.group(1));
                str=str.replace(group,value);
            }
        }
        return str;
    }

    /**
     * 复制文件或文件夹
     * @param srcPath
     * @param destPath
     */
    public static void copyFile(String srcPath, String destPath){
        System.out.println(DateUtil.now() + " " + destPath);
        new File(destPath).getParentFile().mkdirs();
        //复制该文件夹下的所有文件
        cn.hutool.core.io.FileUtil.copy(srcPath,destPath,true);
    }

    /**
     * 写文件
     * @param content
     * @param destPath
     */
    public static void writeFile(String content, String destPath){
        System.out.println(DateUtil.now() + " " + destPath);
        File file = new File(destPath);
        file.getParentFile().mkdirs();
        cn.hutool.core.io.FileUtil.writeString(content,file,"UTF-8");
    }

}
