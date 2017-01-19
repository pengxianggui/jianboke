package com.jianboke.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jianboke.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

	@Query(value = "SELECT * FROM articles a where a.author_id=:authorId AND (a.title LIKE %:filter% OR a.labels LIKE %:filter%) ORDER BY a.created_date", nativeQuery = true)
	public List<Article> queryByFilterAndAuthorId(@Param("filter") String filter, @Param("authorId") Long authorId);

	public List<Article> findAllByAuthorId(Long authorId);
}
