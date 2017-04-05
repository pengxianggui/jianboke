package com.jianboke.web;

import com.jianboke.domain.BookChapterArticle;
import com.jianboke.repository.BookChapterArticleRepository;
import com.jianboke.service.BookChapterArticleService;
import com.jianboke.utils.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    private BookChapterArticleService bookChapterArticleService;

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
    @RequestMapping(value = "/bookChapterArticle/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookChapterArticle removeABookChapterArticle(@PathVariable Long id) {
        log.info("Request to delete a bookChapterArticle, the id is :{}", id);
        if (id == null) return null;
        BookChapterArticle articleModel = bookChapterArticleService.deleteArticleModelById(id);
        return articleModel;
    }

    /**
     * 传入一个章节id，获取该章节下所有的articleModel，注意：这是一个深度搜索，而不仅限于该章节的直接从属的文章
     * @param chapterId
     * @return
     */
    @RequestMapping(value = "/bookChapterArticle/listdeeply/{chapterId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookChapterArticle> getBookChapterArticlesDeeply(@PathVariable Long chapterId) {
        log.info("Request to get All ArticleModel by chapterId deeply, the chapterId is :{}", chapterId);
        if (chapterId == null) return null;
        List<BookChapterArticle> articleModelList = bookChapterArticleService.getAllByChapterIdDeeply(chapterId);
        return articleModelList;
    }

    /**
     * 保存一个articleModel。即归档一篇文章
     * 或者更新，包括sortNum的更新
     * @param articleModel
     * @return
     */
    @RequestMapping(value = "/bookChapterArticle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookChapterArticle> save(@Valid @RequestBody BookChapterArticle articleModel) {
        log.info("Request to save a bookChapterArticle, the bookId is :{}, the parentId is :{}, the articleId is :{}",
                articleModel.getBookId(), articleModel.getParentId(), articleModel.getArticleId());
        if (articleModel == null || articleModel.getBookId() == null || articleModel.getParentId() == null || articleModel.getArticleId() == null) {
            return null; // 传入值有误
        }
        // 避免一篇文章在一个组下被重复归档
        List<BookChapterArticle> temp = bookChapterArticleRepository.getByArticleIdAndParentId(articleModel.getArticleId(), articleModel.getParentId());
        if (temp == null || temp.size() < 1) { // 当前组下没有这篇文章的归档，则可以保存
            System.out.println("当前组下没有这篇文章的归档，则可以保存");
            BookChapterArticle returnResult = bookChapterArticleRepository.saveAndFlush(articleModel);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert("bookChapterArticle", returnResult.getId().toString()))
                    .body(returnResult);
        } else {
            System.out.println("出错了，当前章节下已经归档这篇文章或者重复归档了这篇文章");
            return ResponseEntity.ok(null); // 出错了，当前章节下已经归档这篇文章或者重复归档了这篇文章
        }
    }

    /**
     * 传入一个更新过sortNum的articleModel，重新排序并更新数据库
     * @param id
     * @param newSortNum
     * @return
     */
    @RequestMapping(value = "/bookChapterArticle/updateSortNum/{id}/{newSortNum}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookChapterArticle> updateSortNum(@PathVariable Long id, @PathVariable Integer newSortNum) {
        log.info("update sortNum of the articleModel ,the id is :{}, the new sortNum is :{}", id, newSortNum);
        System.out.println("sbsbs");
        // 非法
        BookChapterArticle returnEntity = bookChapterArticleService.updateArticleModelSortNum(id, newSortNum);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("BookChapterArticle", id.toString()))
                .body(returnEntity);
    }
}
