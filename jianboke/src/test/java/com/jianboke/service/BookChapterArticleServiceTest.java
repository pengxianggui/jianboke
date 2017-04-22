package com.jianboke.service;

import com.jianboke.domain.BookChapterArticle;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by pengxg on 2017/3/16.
 */
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
