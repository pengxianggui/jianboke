package com.jianboke.model;

import com.jianboke.domain.Book;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * Created by pengxg on 2017/4/23.
 */
public class ArticleModel extends BaseModel{

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
    private Set<BookModel> books;
//    private Set<CommentModel> comments;

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

    public Set<BookModel> getBooks() {
        return books;
    }

    public void setBooks(Set<BookModel> books) {
        this.books = books;
    }
//
//    public Set<CommentModel> getComments() {
//        return comments;
//    }
//
//    public void setComments(Set<CommentModel> comments) {
//        this.comments = comments;
//    }

    @Override
    public String toString() {
        return "ArticleModel{" +
                "title='" + title + '\'' +
                ", labels='" + labels + '\'' +
                ", authorId=" + authorId +
                ", secondAuthorId=" + secondAuthorId +
                ", ifOriginal=" + ifOriginal +
                ", originalAuthorName='" + originalAuthorName + '\'' +
                ", ifPublic=" + ifPublic +
                ", ifAllowReprint=" + ifAllowReprint +
                ", ifAllowComment=" + ifAllowComment +
                ", ifAllowSecondAuthor=" + ifAllowSecondAuthor +
                ", ifSetTop=" + ifSetTop +
                ", books=" + books +
//                ", books=" + comments +
                '}';
    }
}
