//package com.jianboke.service;
//
//import com.jianboke.model.UsersModel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Created by pengxg on 2017/8/23.
// */
//@Service("ReplyService")
//public class ReplyServcie {
//
//    private static final Logger log = LoggerFactory.getLogger(ReplyServcie.class);
//
//    /**
//     * 从回复内容中读取 "@username" 以判断回复的对象
//     * @param content
//     * @return
//     */
//    public UsersModel judgeReplyToUser(String content) {
//        Pattern pattern = Pattern.compile("^");
//        Matcher matcher = pattern.matcher(content);
//        if (matcher.find()) {
//            String username = matcher.group(1);
//        }
//        return null;
//    }
//}
