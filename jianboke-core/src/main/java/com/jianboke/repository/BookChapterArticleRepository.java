package com.jianboke.repository;

import com.jianboke.domain.BookChapterArticle;
import com.jianboke.domain.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by pengxg on 2017/3/15.
 */
public interface BookChapterArticleRepository extends JpaRepository<BookChapterArticle, Long>,
        JpaSpecificationExecutor<BookChapterArticle> {

    @Query(value = "select * from book_chapter_articles bca where bca.parent_id =:parentId And bca.sort_num = null", nativeQuery = true)
    List<BookChapterArticle> findNullSortNumByParentId(@Param("parentId") Long parentId);

    @Query(value = "select count(1) from book_chapter_articles bca where bca.parent_id =:parentId", nativeQuery = true)
    int getCountByParentId(@Param("parentId") Long parentId);

    List<BookChapterArticle> findAllByParentId(Long parentId);

    List<BookChapterArticle> findAllByBookId(Long bookId);

    @Query(value = "select * from book_chapter_articles bca where bca.parent_id =:parentId order by bca.sort_num", nativeQuery = true)
    List<BookChapterArticle> findAllByParentIdOrderly(@Param("parentId") Long parentId);

    @Query(value = "select * from book_chapter_articles bca where bca.article_id =:articleId and bca.parent_id =:parentId", nativeQuery = true)
    List<BookChapterArticle> getByArticleIdAndParentId(@Param("articleId") Long articleId, @Param("parentId") Long parentId);

//    @Query(value = "SELECT book_id FROM book_chapter_articles WHERE article_id =:articleId ORDER BY book_id", nativeQuery = true)
    List<BookChapterArticle> findAllByArticleId(Long articleId);

    @Modifying
    @Query("DELETE BookChapterArticle bca WHERE bca.articleId =:articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);

    @Query(value = "SELECT c from Chapter c, BookChapterArticle bca where c.id = bca.parentId and bca.articleId=:articleId and bca.bookId=:bookId")
    List<Chapter> queryAllChapterParentOfArticleUnderTheBook(@Param("bookId") Long bookId, @Param("articleId") Long articleId);
}
