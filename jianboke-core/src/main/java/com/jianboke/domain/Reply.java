package com.jianboke.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

/**
 * 评论回复表
 * Created by pengxg on 2017/8/17.
 */


@Entity
@Table(name = "replys")
public class Reply extends AbstractAuditingEntity {

    @Column(name = "comment_id", nullable = false)
    private Long commentId; // 回复的评论

    @Column(name = "from_uid", nullable = false)
    private Long fromUid; // 当前回复的发起者

    @Column(name = "to_uid")
    private Long toUid; // 当前回复评论的回复对象

    @Column(name = "content", nullable = false)
    private String content; // 内容

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

    public Long getToUid() {
        return toUid;
    }

    public void setToUid(Long toUid) {
        this.toUid = toUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
