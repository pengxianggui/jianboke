package com.jianboke.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * Created by pengxg on 2017/4/23.
 */
public class ArticleModel {

    private Long id;
    private String title;
    private String content;
    private String labels;
    private Long authorId;
    private Long secondAuthorId;
    private boolean ifOriginal;
    private String originalAuthorName;
    private boolean ifPublic;
    private boolean ifAllowReprint;
    private boolean ifAllowComment;
    private boolean ifAllowSecondAuthor;
    private boolean ifSetTop = false;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getSecondAuthorId() {
        return secondAuthorId;
    }

    public void setSecondAuthorId(Long secondAuthorId) {
        this.secondAuthorId = secondAuthorId;
    }

    public boolean isIfOriginal() {
        return ifOriginal;
    }

    public void setIfOriginal(boolean ifOriginal) {
        this.ifOriginal = ifOriginal;
    }

    public String getOriginalAuthorName() {
        return originalAuthorName;
    }

    public void setOriginalAuthorName(String originalAuthorName) {
        this.originalAuthorName = originalAuthorName;
    }

    public boolean isIfPublic() {
        return ifPublic;
    }

    public void setIfPublic(boolean ifPublic) {
        this.ifPublic = ifPublic;
    }

    public boolean isIfAllowReprint() {
        return ifAllowReprint;
    }

    public void setIfAllowReprint(boolean ifAllowReprint) {
        this.ifAllowReprint = ifAllowReprint;
    }

    public boolean isIfAllowComment() {
        return ifAllowComment;
    }

    public void setIfAllowComment(boolean ifAllowComment) {
        this.ifAllowComment = ifAllowComment;
    }

    public boolean isIfAllowSecondAuthor() {
        return ifAllowSecondAuthor;
    }

    public void setIfAllowSecondAuthor(boolean ifAllowSecondAuthor) {
        this.ifAllowSecondAuthor = ifAllowSecondAuthor;
    }

    public boolean isIfSetTop() {
        return ifSetTop;
    }

    public void setIfSetTop(boolean ifSetTop) {
        this.ifSetTop = ifSetTop;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
