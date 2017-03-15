package com.jianboke.web;

import com.jianboke.domain.BookChapterArticle;
import com.jianboke.repository.BookChapterArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by pengxg on 2017/3/15.
 */
@RestController
@RequestMapping("/api")
public class BookChapterArticleController {
    private static final Logger log = LoggerFactory.getLogger(BookChapterArticleController.class);

    @Autowired
    private BookChapterArticleRepository bookChapterArticleRepository;
    /**
     * 传入parentId，查找所有的BookChapterArticle，并返回
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/bookChapterArticle/list/{parentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookChapterArticle> getBookChapterArticles(@PathVariable Long parentId) {
        log.info("REST request to get all bookChapterArticle, the parentId of which is:{}", parentId);
        // TODO 根据parentId请求所有的BookChapterArticle
        // parentId下所有sortNum为null的
        List<BookChapterArticle> nullSortNumList = bookChapterArticleRepository.findNullSortNumByParentId(parentId);
        int count = bookChapterArticleRepository.getCountByParentId(parentId);
        if (nullSortNumList.size() > 0) { // 洗一下: 将没有序号的文章放到最后
            for (int i = 0; i < nullSortNumList.size(); i++) {
                nullSortNumList.get(i).setSortNum(count - 1 - i); // 序号从0开始
                bookChapterArticleRepository.saveAndFlush(nullSortNumList.get(i));
            }
        }
        List<BookChapterArticle> resultLists = bookChapterArticleRepository.findAllByParentId(parentId);
        return resultLists;
    }

    /**
     * 从一本书中移除一篇文章。
     * @param id
     */
    @RequestMapping(value = "/chapter/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookChapterArticle removeABookChapterArticle(@PathVariable Long id) {
        log.info("Request to delete a bookChapterArticle, the id is :{}", id);
        if (id == null) return null;
        BookChapterArticle articleModel = bookChapterArticleRepository.findOne(id);
        bookChapterArticleRepository.delete(articleModel);
        return articleModel;
    }
}
