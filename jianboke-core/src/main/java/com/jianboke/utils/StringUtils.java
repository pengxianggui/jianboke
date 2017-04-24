package com.jianboke.utils;

import org.junit.Test;

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

}
