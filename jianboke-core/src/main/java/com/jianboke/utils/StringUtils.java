package com.jianboke.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

}
