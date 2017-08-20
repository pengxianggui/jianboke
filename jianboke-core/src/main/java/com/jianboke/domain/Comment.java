package com.jianboke.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment")
    @OrderBy(value = "created_date ASC") // 按照创建时间升序排序
    private Set<Reply> replys;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    //
//    public Article getArticle() {
//        return article;
//    }
//
//    public void setArticle(Article article) {
//        this.article = article;
//    }

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
