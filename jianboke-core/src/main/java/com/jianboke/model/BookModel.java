package com.jianboke.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * Created by pengxg on 2017/7/16.
 */
public class BookModel extends BaseModel{
    private String bookName;
    private String bookIntro;
    private Long authorId;
    private String bookCoverPath;
    private Long secondAuthorId;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookIntro() {
        return bookIntro;
    }

    public void setBookIntro(String bookIntro) {
        this.bookIntro = bookIntro;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getBookCoverPath() {
        return bookCoverPath;
    }

    public void setBookCoverPath(String bookCoverPath) {
        this.bookCoverPath = bookCoverPath;
    }

    public Long getSecondAuthorId() {
        return secondAuthorId;
    }

    public void setSecondAuthorId(Long secondAuthorId) {
        this.secondAuthorId = secondAuthorId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
