package com.jianboke.domain.criteria;

import com.jianboke.domain.Book;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pengxg on 2017/7/3.
 */
public class ArticleCriteria {

    private String filter;
    private Long bookId;
    private Book book;
    private String title;
    private String labels;
    private String secondAuthorName;
    private Boolean ifOriginal;
    private Boolean ifPublic;
    private Boolean ifAllowReprint;
    private Boolean ifAllowComment;
    private Boolean ifAllowSecondAuthor;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getSecondAuthorName() {
        return secondAuthorName;
    }

    public void setSecondAuthorName(String secondAuthorName) {
        this.secondAuthorName = secondAuthorName;
    }

    public Boolean isIfOriginal() {
        return ifOriginal;
    }

    public void setIfOriginal(Boolean ifOriginal) {
        this.ifOriginal = ifOriginal;
    }

    public Boolean isIfPublic() {
        return ifPublic;
    }

    public void setIfPublic(Boolean ifPublic) {
        this.ifPublic = ifPublic;
    }

    public Boolean isIfAllowReprint() {
        return ifAllowReprint;
    }

    public void setIfAllowReprint(Boolean ifAllowReprint) {
        this.ifAllowReprint = ifAllowReprint;
    }

    public Boolean isIfAllowComment() {
        return ifAllowComment;
    }

    public void setIfAllowComment(Boolean ifAllowComment) {
        this.ifAllowComment = ifAllowComment;
    }

    public Boolean isIfAllowSecondAuthor() {
        return ifAllowSecondAuthor;
    }

    public void setIfAllowSecondAuthor(Boolean ifAllowSecondAuthor) {
        this.ifAllowSecondAuthor = ifAllowSecondAuthor;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
