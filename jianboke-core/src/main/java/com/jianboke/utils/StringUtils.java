package com.jianboke.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pengxg on 2017/4/22.
 */
public class StringUtils {

    /**
     * 传入length，返回length长度的随机字符串(数字组成)
     * @param length
     * @return
     */
    public static String randomNumberStr(int length) {
        String str = "";
        for (int i = 0; i < length; i++) {
            str += String.valueOf(new Random().nextInt(10));
        }
        return str;
    }

    /**
     * 传入字符串和分隔符，返回list。如： "1,2,3", 分隔符为","， 则返回1,2,3组成的list
     * @param source
     * @param delimiter
     * @return
     */
    public static List<String> split(String source, String delimiter) {
        if (source == null || delimiter == null) {
            return new ArrayList<String>();
        } else {
            List<String> result = new ArrayList<String>();
            for (String s : source.split(delimiter)) {
                result.add(s);
            }
            return result;
        }
    }

    /**
     * 从一段字符串内容中截取 ‘ @username ’ 中的username，并返回
     * @param content
     * @return
     */
    public static String regexUsername(String content) {
        Pattern pattern = Pattern.compile("^@\\S+\\s"); // 匹配 “@xxx ” @开头，空格结尾
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String username = matcher.group(0);
            return username.substring(1);
        }
        return null;
    }

}
