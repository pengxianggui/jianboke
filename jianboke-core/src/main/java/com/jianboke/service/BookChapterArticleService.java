package com.jianboke.service;

import com.jianboke.domain.BookChapterArticle;
import com.jianboke.repository.BookChapterArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pengxg on 2017/3/15.
 */
@Service("BookChapterArticleService")
public class BookChapterArticleService {
    private static final Logger log = LoggerFactory.getLogger(BookChapterArticleService.class);

    @Autowired
    private BookChapterArticleRepository bookChapterArticleRepository;

    /**
     * 根据parentId删除所有的BookChapterArticle
     * @param parentId
     */
    public void deleteAllByParentId(Long parentId) {
        List<BookChapterArticle> bcaList = bookChapterArticleRepository.findAllByParentId(parentId);
        if (bcaList.size() > 0) {
            log.info("delete BookChapterArticleList:{}", bcaList);
            bookChapterArticleRepository.delete(bcaList); // 删除bookChapterArticle
        }
    }

    public List<BookChapterArticle> getAllByParentId(Long id) {
        return bookChapterArticleRepository.findAllByParentId(id);
    }
}
