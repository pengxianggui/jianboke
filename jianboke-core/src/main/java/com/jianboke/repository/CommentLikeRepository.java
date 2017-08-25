package com.jianboke.repository;

import com.jianboke.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by pengxg on 2017/8/24.
 */
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, JpaSpecificationExecutor<CommentLike> {

    Optional<CommentLike> findByCommentIdAndFromUid(Long commentId, Long fromUid);

    @Modifying
    @Query("DELETE CommentLike cl WHERE cl.commentId =:commentId")
    void deleteAllByCommentId(@Param("commentId") Long commentId);
}
