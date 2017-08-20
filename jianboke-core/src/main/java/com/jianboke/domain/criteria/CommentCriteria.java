package com.jianboke.domain.criteria;

/**
 * Created by pengxg on 2017/8/20.
 */
public class CommentCriteria {

    private Long fromUid; // 发表评论的用户
    private Long articleId; // 对应的文章id
    private String orderBy; // 排序的字段

    public Long getFromUid() {
        return fromUid;
    }

    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
