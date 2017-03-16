package com.jianboke.service;

import com.jianboke.domain.BookChapterArticle;
import com.jianboke.domain.Chapter;
import com.jianboke.repository.BookChapterArticleRepository;
import com.jianboke.repository.ChapterRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxg on 2017/3/15.
 */
@Service("BookChapterArticleService")
public class BookChapterArticleService {
    private static final Logger log = LoggerFactory.getLogger(BookChapterArticleService.class);

    @Autowired
    private BookChapterArticleRepository bookChapterArticleRepository;

    @Autowired
    private ChapterRepository chapterRepository;

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

    /**
     * 直接通过parentId查找所有的BookChapterArticle，浅搜索。
     * @param id
     * @return
     */
    public List<BookChapterArticle> getAllByParentId(Long id) {
        return bookChapterArticleRepository.findAllByParentId(id);
    }


    /**
     * 深搜索。根据chapterId查找该章节下所有的article,包括子章节下的articles
     * @param id
     * @return
     */
    public List<BookChapterArticle> getAllByChapterIdDeeply(Long id) {
        List<BookChapterArticle> returnList = new ArrayList<BookChapterArticle>();
        List<Chapter> chapterList = getAllLeafNodeChapter(id); // 拿到chapterId为id的章节下所有叶子章节节点，这些章节都是文章的直接父级
        System.out.println(chapterList.size());
        if(chapterList == null || chapterList.size() == 0) { // 如果id下面没有子章节，则说明此章节即是叶子节点
            return bookChapterArticleRepository.findAllByParentId(id);
        }
        for (Chapter c : chapterList) {
            System.out.println(c.toString());
            List<BookChapterArticle> articleModelList = bookChapterArticleRepository.findAllByParentId(c.getId());
            returnList.addAll(articleModelList);
        }
        return returnList;
    }

    /**
     * 根据chapterId获取该章节下所有的叶子章节节点。
     * @param id
     * @return
     */
    public List<Chapter> getAllLeafNodeChapter(Long id) {
        List<Chapter> returnList = new ArrayList<Chapter>(); // 返回的叶子节点
        List<Chapter> chapterList = chapterRepository.findAllByParentId(id);
        if (chapterList == null || chapterList.size() == 0) return returnList;
        for (Chapter chapter : chapterList) {
            List<Chapter> tempList = getAllLeafNodeChapter(chapter.getId());
            if (tempList == null || tempList.size() == 0) { // 表明当前chapter是叶子节点
                System.out.println("找到叶子节点1：  " + chapter.toString());
                returnList.add(chapter);
            } else {
                tempList.forEach(item->{
                    System.out.println("叶子节点2：  " + item.toString());
                });
                returnList.addAll(tempList);
            }
        }
        return returnList;
    }
}
