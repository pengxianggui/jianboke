package com.jianboke.repository;

import com.jianboke.domain.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by pengxg on 2017/8/24.
 */
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long>, JpaSpecificationExecutor<ArticleLike> {

    Optional<List<ArticleLike>> findAllByArticleId(Long articleId);

    Optional<ArticleLike> findByArticleIdAndFromUid(Long articleId, Long fromUid);

    @Modifying
    @Query("DELETE ArticleLike al WHERE al.articleId =:articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);
}
