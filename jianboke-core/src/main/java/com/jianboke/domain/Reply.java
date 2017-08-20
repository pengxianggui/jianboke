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

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment; // 回复的评论

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "from_uid", nullable = false)
    private User fromUser; // 当前回复的发起者

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "to_uid", nullable = false)
    private User toUser; // 当前回复评论的回复对象

    @Column(name = "content", nullable = false)
    private String content; // 内容

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
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
