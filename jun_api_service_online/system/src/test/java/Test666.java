import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Test666 {


    public static void main(String[] args) {
        List<String> filenames = FileUtil.readLines(new File("D:\\Documents\\Desktop\\MP4.txt"),"UTF-8");
        System.out.println(filenames.size());
        Set<String> sets = Sets.newHashSet();
        filenames.forEach(i->{sets.add(i);});
        System.out.println("不重复的mp4链接："+sets.size());

        List<String> urls = FileUtil.readLines(new File("D:\\Documents\\Desktop\\1110.txt"),"UTF-8");
        Set<String> sets2 = Sets.newHashSet();
        urls.forEach(i->{sets2.add(i);});
        System.out.println("不重复的new链接1："+sets2.size());

        List<String> urlsNew = Lists.newArrayList();
        List<String> urlsExists = Lists.newArrayList();
        for(String url : sets2){
            if(!sets.stream().anyMatch(i->{
                //return url.endsWith(i);
                return FileNameUtil.getName(url).equalsIgnoreCase(i);
               // return url.substring(url.lastIndexOf("/"), url.length()).equalsIgnoreCase(i);
            })){
                urlsNew.add(url);
            }else{
                urlsExists.add(url);
            }
        }
        System.out.println("重复的mp4链接1exitst："+urlsExists.size());
        FileUtil.writeUtf8Lines(urlsExists,"D:\\Documents\\Desktop\\exists111222.txt");
        System.err.println("不重复的mp4链接1new："+urlsNew.size());
        FileUtil.writeUtf8Lines(urlsNew,"D:\\Documents\\Desktop\\new111222.txt");

//        String result = HttpUtil.post("https://gitlab.billjc.com/oauth/token?grant_type=password", "{\n" +
//                "    \"username\": \"wujun82921\",\n" +
//                "    \"password\": \"Abcde123456\"\n" +
//                "}");
//        Console.log(result);
    }

}
