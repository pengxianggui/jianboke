package com.jianboke.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.Set;

/**
 * 文章评论表
 * Created by pengxg on 2017/8/17.
 */

@Entity
@Table(name = "comments")
public class Comment extends AbstractAuditingEntity {

//    @ManyToOne(cascade = {CascadeType.REFRESH})
//    @JoinColumn(name = "article_id", nullable = false)
//    private Article article;
    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Column(name = "from_uid", nullable = false)
    private Long fromUid;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany
    @JoinColumn(name = "comment_id") // 此时joinColumn的列是加到reply中，在Reply中也要声明
    @OrderBy("created_date ASC") // 按照创建时间升序排序
    private Set<Reply> replys;

    @OneToMany
    @JoinColumn(name = "comment_id")
    @OrderBy("created_date ASC")
    private Set<CommentLike> likes; // 评论的喜欢

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getFromUid() {
        return fromUid;
    }

    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Reply> getReplys() {
        return replys;
    }

    public void setReplys(Set<Reply> replys) {
        this.replys = replys;
    }

    public Set<CommentLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<CommentLike> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
