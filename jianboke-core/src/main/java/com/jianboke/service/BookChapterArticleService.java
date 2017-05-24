package com.jianboke.service;

import com.jianboke.domain.Book;
import com.jianboke.domain.BookChapterArticle;
import com.jianboke.domain.Chapter;
import com.jianboke.repository.BookChapterArticleRepository;
import com.jianboke.repository.ChapterRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public List<BookChapterArticle> getAllByBookId(Long bookId){
        return bookChapterArticleRepository.findAllByBookId(bookId);
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

    /**
     * 更新一篇articleModeld的sortNum。排序变更的原则是插队式而非调换式。
     * 即：a b c d e, 将e 的sortNum改为1,则新的排序为a e b c d。
     * @param id
     * @param newSortNum
     * @return 返回这个更新过articleModel
     */
    @Transactional
    public BookChapterArticle updateArticleModelSortNum(Long id, Integer newSortNum) {
        log.info("update a articleModel's sortNum, the articleModel id is :{}", id);
        // 非法
        BookChapterArticle articleModel = bookChapterArticleRepository.findOne(id);
        if (articleModel == null) return null;
        List<BookChapterArticle> articleModelBrothers = bookChapterArticleRepository.findAllByParentId(articleModel.getParentId());
        Integer oldSortNum = articleModel.getSortNum();
        if (newSortNum == oldSortNum) return articleModel; // 原序号和新序号一样
        boolean moveToFront = newSortNum < oldSortNum ? true : false; //是否是向前移动
        if (newSortNum == null || newSortNum >= articleModelBrothers.size()) {
            newSortNum = articleModelBrothers.size() - 1;
        }
        for(BookChapterArticle item : articleModelBrothers) {
            if (item.getId() == id) {
                item.setSortNum(newSortNum);
                articleModel = item;
                continue;
            }
            if (moveToFront) { // 向前移
                if (newSortNum <= item.getSortNum() && item.getSortNum() < oldSortNum) {
                    item.setSortNum(item.getSortNum() + 1);
                }
            } else { // 后移
                if (oldSortNum < item.getSortNum() && item.getSortNum() <= newSortNum) {
                    item.setSortNum(item.getSortNum() - 1);
                }
            }
        }
        bookChapterArticleRepository.save(articleModelBrothers);
        return articleModel;
    }

    /**
     * 从章节中移除一篇文章，返回移除的对象。其中考虑移除文章后的重新排序
     * @param id
     * @return
     */
    public BookChapterArticle deleteArticleModelById(Long id) {
        BookChapterArticle articleModel = bookChapterArticleRepository.findOne(id);
        if (articleModel != null) {
            bookChapterArticleRepository.delete(articleModel);
            clearSortNumByParentId(articleModel.getParentId());
        }
        return articleModel;
    }

    /**
     * 根据parentId清洗一个目录下所有的articleModel的排序
     * @param parentId
     */
    public void clearSortNumByParentId(Long parentId) {
        List<BookChapterArticle> allArticleModelList = bookChapterArticleRepository.findAllByParentIdOrderly(parentId);
        int length = allArticleModelList.size(),
            index = 0;
        for (BookChapterArticle articleModel : allArticleModelList) {
            articleModel.setSortNum(index);
            index++;
        }
        bookChapterArticleRepository.save(allArticleModelList);
    }

    /**
     * 删除某篇文章的归档记录
     * @param articleId
     */
    public void deleteByArticleId(Long articleId) {
        bookChapterArticleRepository.deleteByArticleId(articleId);
    }

}
