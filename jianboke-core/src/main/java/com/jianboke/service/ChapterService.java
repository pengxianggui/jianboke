package com.jianboke.service;

import com.jianboke.domain.Book;
import com.jianboke.domain.BookChapterArticle;
import com.jianboke.domain.Chapter;
import com.jianboke.model.ChapterModel;
import com.jianboke.repository.BookChapterArticleRepository;
import com.jianboke.repository.BookRepository;
import com.jianboke.repository.ChapterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by pengxg on 2017/3/15.
 */
@Service("ChapterService")
public class ChapterService {

    private final Logger log = LoggerFactory.getLogger(ChapterService.class);

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private BookChapterArticleService bookChapterArticleService;

    @Autowired
    private BookRepository bookRepository;

    /**
     * 传入一个ChapterModel,保存它对应的chapter，如果model对应的id为空，则表示新增，如果model对应的id不为空，则表示更新
     * @param model
     * @return
     */
    public ChapterModel saveChapterModel(ChapterModel model) {
        // 保存chapter
        Chapter chapter = chapterRepository.saveAndFlush(modelToChapter(model));
        if (model.getId() == null) { // 新增的节点
            model.setId(chapter.getId());
            model.setExpanded(false); // 新增节点不可展开
            model.setIfCanClick(false); // 新增节点不可点击
            model.setItems(null);
        }
        return model; // 返回model
    }

    /**
     * model转chapter
     * @param model
     * @return
     */
    public Chapter modelToChapter(ChapterModel model) {
        Chapter chapter;
        if (model.getId() == null) {
            chapter = new Chapter();
        } else {
            chapter = chapterRepository.findOne(model.getId());
        }
        chapter.setBookId(model.getBookId());
        chapter.setSortNum(model.getSortNum());
        chapter.setDescription(model.getDescription());
        chapter.setName(model.getGroupName());
        chapter.setParentId(model.getParentId());
        return chapter;
    }

    /**
     * chapter转chapterModel
     * @param chapter
     * @return
     */
    public ChapterModel chapterToModel(Chapter chapter) {
        ChapterModel model = new ChapterModel();
        if (chapter.getId() == null) {
            log.info("chapter's id is null");
            return null;
        }
        model.setId(chapter.getId());
        model.setGroupName(chapter.getName());
        model.setBookId(chapter.getBookId());
        model.setParentId(chapter.getParentId());
        model.setDescription(chapter.getDescription());
        model.setSortNum(chapter.getSortNum());
        return model;
    }

    /**
     * 根据id获取章节名
     * @param id
     * @return
     */
    public String getNameById(Long id) {
        return chapterRepository.getChapterNameById(id);
    }

    /**
     * 强删除一个chapter，强删除表明该chapter下的子章节也会被删除，旗下的表会被撤销归档。
     * @param id
     * @return boolean 返回删除操作的成功与否
     */
//    @Transactional
    public boolean removeChapterByIdStrong(Long id) {
        Chapter chapter = chapterRepository.findOne(id);
        System.out.print(chapter.toString());
        if (removeChapterByIdCycle(id)) {
            log.info("root -> delete the chapter :{}", chapter);
            chapterRepository.delete(chapter);
            bookChapterArticleService.deleteAllByParentId(chapter.getId());
            return true;
        }
        return false;
    }

    /**
     * 迭代删除。传入id，将迭代删除以id为parentId的chapter。
     * @param id
     * @return
     */
    private boolean removeChapterByIdCycle(Long id) {
        List<Chapter> chapterList = chapterRepository.findAllByParentId(id);
        if (chapterList.size() == 0) return true;
        for (Chapter chapter : chapterList) {
            if (removeChapterByIdCycle(chapter.getId())) {
                log.info("node -> delete the chapter:{}", chapter);
                bookChapterArticleService.deleteAllByParentId(chapter.getId());
                chapterRepository.delete(chapter); // 删除chapter
            }
        }
        return true;
    }

    /**
     * 初始化一个本书的章节。当新建一本书时，章节表里并不会有任何数据。直接遍历树会产生错误
     * @param book
     * @return
     */
    public Chapter initChapterForBook(Book book) {
        Chapter c = new Chapter();
        c.setName(book.getBookName());
        c.setBookId(book.getId());
        c.setDescription(null);
        c.setParentId(null);
        c.setSortNum(0);
        chapterRepository.saveAndFlush(c);
        return c;
    }
}
