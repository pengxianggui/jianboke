package com.jianboke.pub.controller;

import com.jianboke.model.ChapterModel;
import com.jianboke.service.TreeOfChapterService;
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
 * Created by pengxg on 2017/8/9.
 */
@RestController
@RequestMapping(value = "/pub")
public class PubBookChapterArticleController {

    private final Logger log = LoggerFactory.getLogger(PubBookChapterArticleController.class);

    @Autowired
    private TreeOfChapterService treeOfChapterService;
    /**
     * 根据bookId得到树，包含article
     * @param bookId
     * @return
     */
    @RequestMapping(value = "/bookChapterArticle/listTreeWithArticle/{bookId}/{articleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChapterModel listTreeWithArticle(@PathVariable Long bookId, @PathVariable Long articleId) {
        log.info("Get tree with article of book which id is :{}, articleId:{}", bookId, articleId);
        List<ChapterModel> cmList = treeOfChapterService.getTreeWithArticle(bookId, articleId);
        return cmList.get(0);
    }
}
