package com.jianboke.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jianboke.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

	@Query(value = "SELECT * FROM articles a where a.author_id=:authorId AND (a.title LIKE %:filter% OR a.labels LIKE %:filter%) ORDER BY a.created_date", nativeQuery = true)
	List<Article> queryByFilterAndAuthorId(@Param("filter") String filter, @Param("authorId") Long authorId);

	List<Article> findAllByAuthorId(Long authorId);

	@Query(value = "SELECT a.id, a.title, bca.parent_id, c.name, bca.sort_num, bca.book_id FROM book_chapter_articles bca,articles a,chapters c WHERE bca.article_id=a.id AND bca.parent_id=c.id AND bca.book_id=:bookId", nativeQuery = true)
	List findArticleModelByBookId(@Param("bookId") Long bookId);

	// 找到一本书的作者，包括第二作者
	@Query(value = "SELECT u.username FROM `users` u, articles a WHERE a.`id` =:articleId AND ( a.`author_id` = u.`id` OR a.`second_author_id` = u.`id`)", nativeQuery = true)
	Optional<List<String>> findAuthorNameByArticleId(@Param("articleId") Long articleId);

//	@Query(value = "select * from articles a, union_book_articles uba where a.id = uba.article_id and (a.author_id =:authorId or a.second_author_id =:authorId) and uba.book_id =:bookId and (a.title like '%:filter%' or a.labels like '%:filter%') ORDER BY a.`last_modified_date` :sort LIMIT :begin, :size")
//    public List<Article> criteriaQueryByBookIdAndAuthorId(@Param("authorId") Long authorId,
//														  @Param("bookId") Long bookId,
//														  @Param("sort") String sort,
//														  @Param("begin") Integer begin,
//														  @Param("size") Integer size,
//														  @Param("filter") String filter);
}
