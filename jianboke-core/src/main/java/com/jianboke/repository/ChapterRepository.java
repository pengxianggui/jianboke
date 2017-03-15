package com.jianboke.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jianboke.domain.Chapter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChapterRepository extends JpaRepository<Chapter, Long>, JpaSpecificationExecutor<Chapter> {

	public List<Chapter> findAllByBookId(Long bookId);

	@Query(value = "select name from chapters where id =:id", nativeQuery = true)
	public String getChapterNameById(@Param("id") Long id);

	public List<Chapter> findAllByParentId(Long parentId);
}
