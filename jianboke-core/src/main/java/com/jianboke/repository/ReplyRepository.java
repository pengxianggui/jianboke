package com.jianboke.repository;

import com.jianboke.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by pengxg on 2017/8/17.
 */
public interface ReplyRepository extends JpaRepository<Reply, Long>, JpaSpecificationExecutor<Reply> {

    @Modifying
    @Query("DELETE Reply r WHERE r.commentId =:commentId")
    void deleteAllByCommentId(@Param("commentId") Long commentId);
}
