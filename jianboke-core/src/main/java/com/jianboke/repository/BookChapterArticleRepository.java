package com.jianboke.repository;

import com.jianboke.domain.BookChapterArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by pengxg on 2017/3/15.
 */
public interface BookChapterArticleRepository extends JpaRepository<BookChapterArticle, Long>,
        JpaSpecificationExecutor<BookChapterArticle> {

    @Query(value = "select * from book_chapter_articles bca where bca.parent_id =:parentId And bca.sort_num = null", nativeQuery = true)
    public List<BookChapterArticle> findNullSortNumByParentId(@Param("parentId") Long parentId);

    @Query(value = "select count(1) from book_chapter_articles bca where bca.parent_id =:parentId", nativeQuery = true)
    public int getCountByParentId(@Param("parentId") Long parentId);

    public List<BookChapterArticle> findAllByParentId(Long parentId);
}
