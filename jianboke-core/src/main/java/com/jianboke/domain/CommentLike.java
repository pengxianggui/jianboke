package com.jianboke.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by pengxg on 2017/8/24.
 */
@Entity
@Table(name = "comment_likes")
public class CommentLike extends AbstractAuditingEntity {

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "from_uid", nullable = false)
    private Long fromUid;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getFromUid() {
        return fromUid;
    }

    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
