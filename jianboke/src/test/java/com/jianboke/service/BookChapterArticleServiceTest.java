package com.jianboke.service;

import com.jianboke.JBKApplication;
import com.jianboke.domain.BookChapterArticle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by pengxg on 2017/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JBKApplication.class) // 获取ApplicationContxt
@WebAppConfiguration // 自动装配bean
public class BookChapterArticleServiceTest {

    @Autowired
    BookChapterArticleService bookChapterArticleService;

    @Test
    public void getAllByChapterIdDeeply() {
        List<BookChapterArticle> list = bookChapterArticleService.getAllByChapterIdDeeply(1l);
        list.forEach(item -> {
            System.out.println(item.toString());
        });
    }
}
